package io.billie.organisations.resource.rest.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.organisations.model.LegalEntityType
import io.billie.organisations.service.model.ContactDetails
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.NotBlank

data class OrganizationCreationRequest(
    @field:NotBlank val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("date_founded") val foundedDate: LocalDate,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String,
    @JsonProperty("vat_number") val vatNumber: String?,
    @JsonProperty("registration_number") val registrationNumber: String?,
    @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
    @JsonProperty("contact_details") val contactDetails: ContactDetails,
)
