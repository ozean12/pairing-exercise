package io.billie.organisations.data

import io.billie.countries.model.CountryResponse
import io.billie.organisations.viewmodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.ResultSet
import java.util.*

@Repository
class OrganisationRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findOrganisations(): List<OrganisationResponse> {
        return jdbcTemplate.query(organisationQuery(), organisationMapper())
    }

    @Transactional
    fun create(organisation: OrganisationRequest): UUID {
        valuesValid(organisation)
        addressValid(organisation.address)

        val contactDetailsId = createContactDetails(organisation.contactDetails)
        val addressId = createAddress(organisation.address)
        return createOrganisation(organisation, contactDetailsId, addressId)
    }

    private fun addressValid(address: AddressRequest): Unit {
        val reply: Int? = jdbcTemplate.query(
            "select count(name) from organisations_schema.cities c WHERE c.name = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            address.city
        )

        if (reply == null || reply < 1) {
            throw InvalidAddress(address.city)
        }
    }

    private fun valuesValid(organisation: OrganisationRequest) {
        if (organisation.countryCode != organisation.address.countryCode)
            throw MismatchedCountryCode(organisation.countryCode, organisation.address.countryCode)

        val reply: Int? = jdbcTemplate.query(
            "select count(country_code) from organisations_schema.countries c WHERE c.country_code = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            organisation.countryCode
        )

        if ((reply == null) || (reply < 1)) {
            throw UnableToFindCountry(organisation.countryCode)
        }
    }

    private fun createOrganisation(org: OrganisationRequest, contactDetailsId: UUID, addressId: UUID): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "INSERT INTO organisations_schema.organisations (" +
                            "name, " +
                            "date_founded, " +
                            "country_code, " +
                            "vat_number, " +
                            "registration_number, " +
                            "legal_entity_type, " +
                            "contact_details_id, " +
                            "address_id" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setString(1, org.name)
                ps.setDate(2, Date.valueOf(org.dateFounded))
                ps.setString(3, org.countryCode)
                ps.setString(4, org.VATNumber)
                ps.setString(5, org.registrationNumber)
                ps.setString(6, org.legalEntityType.toString())
                ps.setObject(7, contactDetailsId)
                ps.setObject(8, addressId)
                ps
            }, keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createAddress(address: AddressRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.address " +
                            "(" +
                            "address1, " +
                            "address2, " +
                            "country_code, " +
                            "city, " +
                            "state, " +
                            "postal_code" +
                            ") values(?,?,?,?,?,?)",
                    arrayOf("id")
                )
                ps.setString(1, address.address1)
                ps.setString(2, address.address2)
                ps.setString(3, address.countryCode)
                ps.setString(4, address.city.trim())
                ps.setString(5, address.state.trim())
                ps.setString(6, address.postalCode)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun createContactDetails(contactDetails: ContactDetailsRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.contact_details " +
                            "(" +
                            "phone_number, " +
                            "fax, " +
                            "email" +
                            ") values(?,?,?)",
                    arrayOf("id")
                )
                ps.setString(1, contactDetails.phoneNumber)
                ps.setString(2, contactDetails.fax)
                ps.setString(3, contactDetails.email)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun organisationQuery() =
        """SELECT
             o.id as id,
             o.name as name,
             o.date_founded as date_founded,
             o.country_code as country_code,
             c.id as country_id, c.name as country_name,
             o.VAT_number as VAT_number,
             o.registration_number as registration_number,
             o.legal_entity_type as legal_entity_type,
             o.contact_details_id as contact_details_id,
             cd.phone_number as phone_number,
             cd.fax as fax,
             cd.email as email,
             o.address_id as address_id,
             a.address1 as address1,
             a.address2 as address2,
             a.country_code as a_country_code,
             a.city as city,
             a.state as state,
             a.postal_code as postal_code
           FROM
             organisations_schema.organisations o
           INNER JOIN
             organisations_schema.contact_details cd ON o.contact_details_id::uuid = cd.id::uuid
           INNER JOIN
             organisations_schema.countries c ON o.country_code = c.country_code
           INNER JOIN
             organisations_schema.address a ON o.address_id::uuid = a.id::uuid"""

    private fun organisationMapper() = RowMapper<OrganisationResponse> { it: ResultSet, _: Int ->
        OrganisationResponse(
            it.getObject("id", UUID::class.java),
            it.getString("name"),
            Date(it.getDate("date_founded").time).toLocalDate(),
            mapCountry(it),
            it.getString("vat_number"),
            it.getString("registration_number"),
            LegalEntityType.valueOf(it.getString("legal_entity_type")),
            mapContactDetails(it),
            mapAddress(it)
        )
    }

    private fun mapAddress(it: ResultSet): AddressResponse {
        return AddressResponse(
            UUID.fromString(it.getString("address_id")),
            it.getString("address1"),
            it.getString("address2"),
            it.getString("a_country_code"),
            it.getString("city"),
            it.getString("state"),
            it.getString("postal_code"),
        )
    }

    private fun mapContactDetails(it: ResultSet): ContactDetails {
        return ContactDetails(
            UUID.fromString(it.getString("contact_details_id")),
            it.getString("phone_number"),
            it.getString("fax"),
            it.getString("email")
        )
    }

    private fun mapCountry(it: ResultSet): CountryResponse {
        return CountryResponse(
            it.getObject("country_id", UUID::class.java),
            it.getString("country_name"),
            it.getString("country_code")
        )
    }

}
