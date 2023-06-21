package io.billie.organisations.data

import io.billie.countries.model.toCityResponse
import io.billie.countries.model.toCountryResponse
import io.billie.organisations.viewmodel.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
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
        val addressDetailsId = organisation.addressDetails?.let { createAddressDetails(it) }
        return createOrganisation(organisation, contactDetailsId, addressDetailsId)
    }

    private fun createOrganisation(org: OrganisationRequest, contactDetailsId: UUID, addressDetailsId: UUID?): UUID {
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
                            "address_details_id" +
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
                ps.setObject(8, addressDetailsId)
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

    private fun createAddressDetails(addressDetails: AddressDetailsRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                val ps = connection.prepareStatement(
                    "insert into organisations_schema.address_details " +
                            "(" +
                            "city_id, " +
                            "country_code, " +
                            "zip_code, " +
                            "address_string" +
                            ") values(?,?,?,?)",
                    arrayOf("id")
                )
                ps.setString(1, addressDetails.cityId)
                ps.setString(2, addressDetails.countryCode)
                ps.setString(3, addressDetails.zipCode)
                ps.setString(4, addressDetails.addressString)
                ps
            },
            keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun organisationQuery() = "select " +
            "o.id as id, " +
            "o.name as name, " +
            "o.date_founded as date_founded, " +
            "o.country_code as organisation_country_code, " +
            "c.id as country_id, " +
            "c.name as country_name, " +
            "o.VAT_number as VAT_number, " +
            "o.registration_number as registration_number," +
            "o.legal_entity_type as legal_entity_type," +
            "o.contact_details_id as contact_details_id, " +
            "o.address_details_id as address_details_id, " +
            "cd.phone_number as phone_number, " +
            "cd.fax as fax, " +
            "cd.email as email, " +
            "ad.city_id as city_id " +
            "from " +
            "organisations_schema.organisations o " +
            "INNER JOIN organisations_schema.contact_details cd on o.contact_details_id::uuid = cd.id::uuid " +
            "INNER JOIN organisations_schema.countries c on o.country_code = c.country_code " +
            "LEFT JOIN organisations_schema.address_details ad on o.address_details_id::uuid = ad.id::uuid " +
            "LEFT JOIN organisations_schema.cities ct on ad.city_id::uuid = ct.id::uuid "

    private fun organisationMapper() = RowMapper<OrganisationResponse> { it: ResultSet, _: Int ->
        OrganisationResponse(
            it.getObject("id", UUID::class.java),
            it.getString("name"),
            Date(it.getDate("date_founded").time).toLocalDate(),
            toCountryResponse(it),
            it.getString("vat_number"),
            it.getString("registration_number"),
            LegalEntityType.valueOf(it.getString("legal_entity_type")),
            mapContactDetails(it),
            mapAddressDetails(it),
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

    private fun mapAddressDetails(resultSet: ResultSet): AddressDetails? {
        if (resultSet.getString("address_details_id") == null || resultSet.wasNull()) {
            return null
        }
        return toAddressDetails(resultSet, toCountryResponse(resultSet), toCityResponse(resultSet))
    }

}
