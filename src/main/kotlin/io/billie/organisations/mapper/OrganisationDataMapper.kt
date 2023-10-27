package io.billie.organisations.mapper

import io.billie.countries.model.CountryResponse
import io.billie.organisations.dto.OrganisationResponse
import io.billie.organisations.model.ContactDetails
import io.billie.organisations.model.LegalEntityType
import org.springframework.jdbc.core.RowMapper
import java.sql.Date
import java.sql.ResultSet
import java.util.*

 fun mapOrganisation(it: ResultSet) =
    OrganisationResponse(
            it.getObject("organisation_id", UUID::class.java),
            it.getString("organisation_name"),
            Date(it.getDate("organisation_date_founded").time).toLocalDate(),
            mapCountry(it),
            it.getString("organisation_vat_number"),
            it.getString("organisation_registration_number"),
            LegalEntityType.valueOf(it.getString("organisation_legal_entity_type")),
            mapContactDetails(it)
    )

 fun mapContactDetails(it: ResultSet): ContactDetails {
    return ContactDetails(
            UUID.fromString(it.getString("organisation_contact_details_id")),
            it.getString("cd_phone_number"),
            it.getString("cd_fax"),
            it.getString("cd_email")
    )
}
 fun mapCountry(it: ResultSet): CountryResponse {
    return CountryResponse(
            it.getObject("country_id", UUID::class.java),
            it.getString("country_name"),
            it.getString("country_code")
    )
}