package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Table("ORGANISATIONS")
data class OrganisationRequest(
    @field:NotBlank val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("date_founded") val dateFounded: LocalDate,
    @JsonProperty("vat_number") val VATNumber: String?,
    @JsonProperty("registration_number") val registrationNumber: String?,
    @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
    @JsonProperty("contact_details") val contactDetails: ContactDetailsRequest,
    @field:Valid @JsonProperty("address") val address: AddressRequest,
)
