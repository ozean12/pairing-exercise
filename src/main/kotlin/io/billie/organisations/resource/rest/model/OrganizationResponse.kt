package io.billie.organisations.resource.rest.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.address.model.Country
import io.billie.organisations.model.LegalEntityType
import io.billie.organisations.service.model.ContactDetails
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

data class OrganizationResponse(
    val id: UUID,
    val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy") @JsonProperty("founded_date")
    val foundedDate: LocalDate,
    @JsonProperty("vat_number") val vatNumber: String?,
    @JsonProperty("registration_number") val registrationNumber: String?,
    @JsonProperty("legal_entity_type") val legalEntityType: LegalEntityType,
    @JsonProperty("contact_details") val contactDetails: ContactDetails,
    val country: Country,
)
