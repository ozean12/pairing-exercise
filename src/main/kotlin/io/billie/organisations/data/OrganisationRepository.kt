package io.billie.organisations.data

import io.billie.countries.model.CountryResponse
import io.billie.organisations.viewmodel.AddressResponse
import io.billie.organisations.viewmodel.AddressRequest
import io.billie.organisations.viewmodel.ContactDetails
import io.billie.organisations.viewmodel.ContactDetailsRequest
import io.billie.organisations.viewmodel.LegalEntityType
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.UUID


@Repository
class OrganisationRepository(private val jdbcTemplate: JdbcTemplate) {

    @Transactional(readOnly = true)
    fun findOrganisations(): List<OrganisationResponse> {
        return jdbcTemplate.query(organisationQuery(), organisationMapper())
    }

    @Transactional
    fun create(organisation: OrganisationRequest): UUID {
        isInputValid(organisation)
        val contactDetailsId: UUID = createContactDetails(organisation.contactDetails)
        val addressId: UUID = createAddress(organisation.address)
        return createOrganisation(organisation, contactDetailsId, addressId)
    }

    private fun isInputValid(organisation: OrganisationRequest) {
        with(organisation.address) {
            if (!isCountryCodeValid(countryCode)) {
                throw UnableToFindCountry(countryCode)
            }

            if (!isCityValid(countryCode, city)) {
                throw UnableToFindCity(countryCode, city)
            }
        }
    }

    private fun isCountryCodeValid(countryCode: String): Boolean =
        isInputValid(
            // language=PostgreSQL
            "SELECT count(country_code) FROM organisations_schema.countries c WHERE c.country_code = ?",
            countryCode
        )

    private fun isCityValid(countryCode: String, city: String): Boolean =
        isInputValid(
            // language=PostgreSQL
            "SELECT count(name) FROM organisations_schema.cities c WHERE c.country_code = ? AND c.name = ?",
            countryCode,
            city
        )

    private fun isInputValid(sql: String, vararg args: Any): Boolean {
        val reply: Int? = jdbcTemplate.query(
            sql,
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            *args
        )
        return (reply != null) && (reply > 0)
    }

    private fun createOrganisation(org: OrganisationRequest, contactDetailsId: UUID, addressId: UUID): UUID {
        // language=PostgreSQL
        val sql = """
            INSERT INTO organisations_schema.organisations (name,
                                                date_founded,
                                                country_code,
                                                vat_number,
                                                registration_number,
                                                legal_entity_type,
                                                contact_details_id,
                                                address_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        return createInDatabase(sql) {
            setString(1, org.name)
            setDate(2, Date.valueOf(org.dateFounded))
            setString(3, org.address.countryCode)
            setString(4, org.VATNumber)
            setString(5, org.registrationNumber)
            setString(6, org.legalEntityType.toString())
            setObject(7, contactDetailsId)
            setObject(8, addressId)
            this
        }
    }

    private fun createContactDetails(contactDetails: ContactDetailsRequest): UUID {
        // language=PostgreSQL
        val sql = """
            INSERT INTO organisations_schema.contact_details (phone_number,
                                                 fax,
                                                 email)
            VALUES (?, ?, ?)
        """.trimIndent()

        return createInDatabase(sql) {
            setString(1, contactDetails.phoneNumber)
            setString(2, contactDetails.fax)
            setString(3, contactDetails.email)
            this
        }
    }

    private fun createAddress(address: AddressRequest): UUID {
        // language=PostgreSQL
        val sql = """
            INSERT INTO organisations_schema.addresses (address_line_1,
                                                        address_line_2,
                                                        zip_code,
                                                        city,
                                                        country_code)
            values (?, ?, ?, ?, ?)
        """.trimIndent()

        return createInDatabase(sql) {
            setString(1, address.addressLine1)
            setString(2, address.addressLine2)
            setString(3, address.zipCode)
            setString(4, address.city)
            setString(5, address.countryCode)
            this
        }
    }

    private fun createInDatabase(
        sql: String,
        quertyParamProvider: PreparedStatement.() -> PreparedStatement
    ): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                connection.prepareStatement(sql, arrayOf("id"))
                    .also { quertyParamProvider(it) }
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun organisationQuery() =
        // language=PostgreSQL
        """SELECT 
            o.id AS id,
            o.name AS name,
            o.date_founded AS date_founded,
            o.country_code AS country_code,
            c.id AS country_id,
            c.name AS country_name,
            o.VAT_number AS VAT_number,
            o.registration_number AS registration_number,
            o.legal_entity_type AS legal_entity_type,
            o.contact_details_id AS contact_details_id,
            cd.phone_number AS phone_number,
            cd.fax AS fax,
            cd.email AS email,
            ad.id AS address_id,
            ad.address_line_1,
            ad.address_line_2,
            ad.zip_code AS address_zip_code,
            ad.city AS address_city,
            ad.country_code AS address_country_code
            FROM
            organisations_schema.organisations o
            INNER JOIN organisations_schema.contact_details cd ON o.contact_details_id::uuid = cd.id::uuid
            INNER JOIN organisations_schema.countries c ON o.country_code = c.country_code
            LEFT JOIN organisations_schema.addresses ad ON o.address_id::uuid = ad.id
            """.trimIndent()

    private fun organisationMapper() = RowMapper<OrganisationResponse> { it: ResultSet, _: Int ->
        OrganisationResponse(
            id = it.getObject("id", UUID::class.java),
            name = it.getString("name"),
            dateFounded = Date(it.getDate("date_founded").time).toLocalDate(),
            VATNumber = it.getString("vat_number"),
            registrationNumber = it.getString("registration_number"),
            legalEntityType = LegalEntityType.valueOf(it.getString("legal_entity_type")),
            contactDetails = mapContactDetails(it),
            address = mapAddress(it)
        )
    }

    private fun mapContactDetails(it: ResultSet) =
        ContactDetails(
            id = it.getObject("id", UUID::class.java),
            phoneNumber = it.getString("phone_number"),
            fax = it.getString("fax"),
            email = it.getString("email"),
        )

    private fun mapAddress(it: ResultSet) =
        AddressResponse(
            id = it.getObject("address_id", UUID::class.java),
            addressLine1 = it.getString("address_line_1"),
            addressLine2 = it.getString("address_line_2"),
            zipCode = it.getString("address_zip_code"),
            city = it.getString("address_city"),
            country = mapCountry(it)
        )

    private fun mapCountry(it: ResultSet) =
        CountryResponse(
            id = it.getObject("country_id", UUID::class.java),
            name = it.getString("country_name"),
            countryCode = it.getString("country_code")
        )
}
