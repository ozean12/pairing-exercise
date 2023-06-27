package io.billie.organisations.data

import io.billie.countries.model.CountryResponse
import io.billie.organisations.viewmodel.*
import org.postgresql.util.PGobject
import org.json.JSONObject
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

// use JPA instead of manually writing queries
// no unit tests for this file

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
        if(!valuesValid(organisation)) {
            throw UnableToFindCountry(organisation.countryCode)
        }
        val contactDetailsId: UUID = createContactDetails(organisation.contactDetails)
        val addressId = createAddress(organisation.address)
        return createOrganisation(organisation, contactDetailsId, addressId)
    }

    private fun valuesValid(organisation: OrganisationRequest): Boolean {
        val addressCountryCode = organisation.address.countryCode
        return countryCodeValid(organisation.countryCode) && countryCodeValid(addressCountryCode)
    }

    private fun countryCodeValid(countryCode: String): Boolean {
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

    // contact details creation can be moved to a separate repository
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

    private fun createAddress(address: AddressRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val addressData = hashMapOf<String,Any?>("line1" to address.line1,
                    "country_code" to address.countryCode,
                    "city" to address.city,
                    "post_code" to address.postCode,
                    "line2" to address.line2
                )
//                val jsonObject = PGobject()
//                jsonObject.setType("json")
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.addresses (data) VALUES (?) ",
                    arrayOf("id")
                )
                val jsonObject = PGobject()
                jsonObject.type = "json"
                jsonObject.value = JSONObject(addressData).toString()
                ps.setObject(1, jsonObject)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun organisationQuery() = """
        select
            o.id as id,
            o.name as name,
            o.date_founded as date_founded,
            o.country_code as country_code,
            c.id as country_id,
            c.name as country_name,
            o.VAT_number as VAT_number,
            o.registration_number as registration_number,
            o.legal_entity_type as legal_entity_type,
            o.contact_details_id as contact_details_id,
            cd.phone_number as phone_number,
            cd.fax as fax,
            cd.email as email,
            o.address_id as address_id,
            oa.data -> 'line1' as address_line1,
            oa.data -> 'line2' as address_line2,
            oa.data -> 'city' as address_city,
            oa.data -> 'post_code' as address_post_code,
            oa.data -> 'country_code' as address_country_code
        from
            organisations_schema.organisations o
            INNER JOIN organisations_schema.contact_details cd on o.contact_details_id::uuid = cd.id::uuid
            INNER JOIN organisations_schema.addresses oa on o.address_id::uuid = oa.id::uuid
            INNER JOIN organisations_schema.countries c on o.country_code = c.country_code
    """.trimIndent()

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

    private fun mapAddress(it: ResultSet): Address? {
        val addressId: String = it.getString("address_id") ?: return null
        return Address(
            UUID.fromString(addressId),
            it.getString("address_line1"),
            it.getString("address_line2"),
            it.getString("address_post_code"),
            it.getString("address_city"),
            it.getString("address_country_code")
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
