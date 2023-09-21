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
        if(!valuesValid(organisation)) {
            throw UnableToFindCountry(organisation.countryCode)
        }
        val id: UUID = createContactDetails(organisation.contactDetails)
        return createOrganisation(organisation, id)
    }

    private fun valuesValid(organisation: OrganisationRequest): Boolean {
        val reply: Int? = jdbcTemplate.query(
            "select count(country_code) from organisations_schema.countries c WHERE c.country_code = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            organisation.countryCode
        )
        return (reply != null) && (reply > 0)
    }

    private fun createOrganisation(org: OrganisationRequest, contactDetailsId: UUID): UUID {
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
                            "contact_details_id" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?)",
                    arrayOf("id")
                )
                ps.setString(1, org.name)
                ps.setDate(2, Date.valueOf(org.dateFounded))
                ps.setString(3, org.countryCode)
                ps.setString(4, org.VATNumber)
                ps.setString(5, org.registrationNumber)
                ps.setString(6, org.legalEntityType.toString())
                ps.setObject(7, contactDetailsId)
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

    private fun createAddress(address: AddressRequest): UUID {
        val keyHolder: KeyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
                {connection ->
                    val ps = connection.prepareStatement(
                            "insert into organisations_schema.addresses " +
                                    "(" +
                                    "city, " +
                                    "postcode, "+
                                    "address_line_1, "+
                                    "address_line_2, " +
                                    "organisation_id, "+
                                    "city_id"+
                                    ") values(?, ?, ?, ?, ?, ?)",
                            arrayOf("id")
                    )
                    ps.setString(1, address.city)
                    ps.setString(2, address.postcode)
                    ps.setString(3, address.addressLine1)
                    ps.setString(4, address.addressLine2)
                    ps.setObject(5, address.organisationId)
                    ps.setObject(6, address.cityId)

                    ps
                },
                keyHolder
        )
        return keyHolder.getKeyAs(UUID::class.java)!!
    }

    private fun validateAddress(address: AddressRequest): Boolean {

        val orgExists: Boolean? = jdbcTemplate.query(
                "select exists(select 1 from organisations_schema.organisations o WHERE o.id = ?)",
                ResultSetExtractor {
                    it.next()
                    it.getBoolean(1)
                },
                address.organisationId
        )

        if (!orgExists!!)
            throw UnableToFindOrganisation(address.organisationId)

        val cityExists: Boolean? = jdbcTemplate.query(
                "select exists(select 1 from organisations_schema.cities c WHERE c.id = ?)",
                ResultSetExtractor {
                    it.next()
                    it.getBoolean(1)
                },
                address.cityId
        )

        if (!cityExists!!)
            throw UnableToFindCity(address.cityId)

        return true
    }

    @Transactional
    fun addAddress(address: AddressRequest): UUID{
        validateAddress(address)
        return createAddress(address)
    }

    private fun organisationQuery() = "select " +
            "o.id as id, " +
            "o.name as name, " +
            "o.date_founded as date_founded, " +
            "o.country_code as country_code, " +
            "c.id as country_id, " +
            "c.name as country_name, " +
            "o.VAT_number as VAT_number, " +
            "o.registration_number as registration_number," +
            "o.legal_entity_type as legal_entity_type," +
            "o.contact_details_id as contact_details_id, " +
            "cd.phone_number as phone_number, " +
            "cd.fax as fax, " +
            "cd.email as email, " +
            "a.city as city, "+
            "a.postcode as postcode, "+
            "a.address_line_1 as address_line_1, "+
            "a.address_line_2 as address_line_2 "+
            "from " +
            "organisations_schema.organisations o " +
            "INNER JOIN organisations_schema.contact_details cd on o.contact_details_id::uuid = cd.id::uuid " +
            "INNER JOIN organisations_schema.countries c on o.country_code = c.country_code "+
            "LEFT JOIN organisations_schema.addresses a on o.id = a.organisation_id"

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

    private fun mapAddress(it: ResultSet): AddressResponse {
        return AddressResponse(
                it.getString("city"),
                it.getString("postcode"),
                it.getString("address_line_1"),
                it.getString("address_line_2")
        )
    }
    @Transactional(readOnly = true)
    fun findAdressesByOrgCode(orgCode: String): List<AddressResponse> {
        return jdbcTemplate.query("SELECT city, postcode, address_line_1, address_line_2 FROM organisations_schema.addresses WHERE organisation_id = ?",
                addressesMapper(),
                UUID.fromString(orgCode))
    }

    private fun addressesMapper() = RowMapper<AddressResponse> {it: ResultSet, _: Int ->
     AddressResponse(
             it.getString("city"),
             it.getString("postcode"),
             it.getString("address_line_1"),
             it.getString("address_line_2")
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
