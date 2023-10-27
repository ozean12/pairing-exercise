package io.billie.organisations.data

import io.billie.organisations.dto.ContactDetailsRequest
import io.billie.organisations.dto.OrganisationRequest
import io.billie.organisations.dto.OrganisationResponse
import io.billie.organisations.mapper.mapOrganisation
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
        return jdbcTemplate.query(organisationQuery(),
                organisationResponseMapper()
        )
    }

    private fun organisationResponseMapper() = RowMapper<OrganisationResponse> { it: ResultSet, _: Int ->
        mapOrganisation(it)
    }

    @Transactional
    fun create(organisation: OrganisationRequest): UUID {
        val id: UUID = createContactDetails(organisation.contactDetails)
        return createOrganisation(organisation, id)
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

    private fun organisationQuery() = """
        SELECT
            o.id as organisation_id,
            o.name as organisation_name,
            o.date_founded as organisation_date_founded,
            o.country_code as organisation_country_code,
            c.id as country_id,
            c.name as country_name,
            c.country_code as country_code,
            o.VAT_number as organisation_vat_number,
            o.registration_number as organisation_registration_number,
            o.legal_entity_type as organisation_legal_entity_type,
            o.contact_details_id as organisation_contact_details_id,
            cd.phone_number as cd_phone_number,
            cd.fax as cd_fax,
            cd.email as cd_email
         FROM
            organisations_schema.organisations o
         INNER JOIN
            organisations_schema.contact_details cd ON o.contact_details_id::uuid = cd.id::uuid
         INNER JOIN
            organisations_schema.countries c ON o.country_code = c.country_code
    """.trimIndent()
}
