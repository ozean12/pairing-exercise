package io.billie.organisations.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.countries.model.CountryResponse
import io.billie.organisations.model.ContactDetails
import io.billie.organisations.model.LegalEntityType
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

@Table("ORGANISATIONS")
data class OrganisationResponse(
        val id: UUID,
        val name: String,
        @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("date_founded") val dateFounded: LocalDate,
        val country: CountryResponse,
        @JsonProperty("vat_number") val VATNumber: String?,
        @JsonProperty("registration_number") val registrationNumber: String?,
        @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
        @JsonProperty("contact_details") val contactDetails: ContactDetails,
)
