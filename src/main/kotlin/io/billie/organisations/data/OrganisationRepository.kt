package io.billie.organisations.data

import io.billie.countries.model.CountryResponse
import io.billie.organisations.viewmodel.*
import org.intellij.lang.annotations.Language
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
        val contactDetailsId: UUID = createContactDetails(organisation.contactDetails)
        val mainAddressId: UUID? = organisation.mainAddress?.let { createAddress(it) }
        return createOrganisation(organisation, contactDetailsId, mainAddressId)
    }

    @Transactional
    fun countryExists(countryCode: String): Boolean {
        val reply: Int? = jdbcTemplate.query(
            "select count(country_code) from organisations_schema.countries c WHERE c.country_code = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            countryCode
        )
        return (reply != null) && (reply > 0)
    }

    private fun createOrganisation(org: OrganisationRequest, contactDetailsId: UUID, mainAddressId: UUID?): UUID {
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
                        "contact_details_id," +
                        "main_address_id" +
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
                ps.setObject(8, mainAddressId)
                ps
            }, keyHolder
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

    private fun createAddress(address: AddressCreateRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.address " +
                        "(" +
                        "country_code," +
                        "city_name," +
                        "zip_code," +
                        "street," +
                        "house_number," +
                        "comment" +
                        ") values(?,?,?,?,?,?)",
                    arrayOf("id")
                )
                ps.setString(1, address.countryCode)
                ps.setString(2, address.cityName)
                ps.setString(3, address.zipCode)
                ps.setString(4, address.street)
                ps.setString(5, address.houseNumber)
                ps.setString(6, address.comment)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    @Language("PostgreSQL")
    private fun organisationQuery() =
        """
        |select 
        |    o.id as id, 
        |    o.name as name, 
        |    o.date_founded as date_founded, 
        |    o.country_code as country_code, 
        |    c.id as country_id, 
        |    c.name as country_name, 
        |    o.VAT_number as VAT_number, 
        |    o.registration_number as registration_number,
        |    o.legal_entity_type as legal_entity_type,
        |    o.contact_details_id as contact_details_id, 
        |    cd.phone_number as phone_number, 
        |    cd.fax as fax, 
        |    cd.email as email,
        |    a.id as address_id,
        |    a.country_code as address_country_code,
        |    a.city_name as address_city_name,
        |    a.zip_code as address_zip_code,
        |    a.street as address_street,
        |    a.house_number as address_house_number,
        |    a.comment as address_comment
        |from organisations_schema.organisations o 
        |    INNER JOIN organisations_schema.contact_details cd on o.contact_details_id::uuid = cd.id::uuid 
        |    INNER JOIN organisations_schema.countries c on o.country_code = c.country_code
        |    LEFT JOIN organisations_schema.address a on o.main_address_id = a.id
        |""".trimMargin()

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

    private fun mapAddress(it: ResultSet): Address? {
        val addressId: UUID = it.getObject("address_id", UUID::class.java)
            ?: return null
        return Address(
            id = addressId,
            countryCode = it.getString("address_country_code"),
            cityName = it.getString("address_city_name"),
            zipCode = it.getString("address_zip_code"),
            street = it.getString("address_street"),
            houseNumber = it.getString("address_house_number"),
            comment = it.getString("address_comment"),
        )
    }
}
