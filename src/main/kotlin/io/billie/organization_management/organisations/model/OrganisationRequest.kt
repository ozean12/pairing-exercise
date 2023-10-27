package io.billie.organization_management.organisations.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class OrganisationRequest(
    @field:NotBlank
    val name: String,
    @Schema(type = "string", pattern = "dd/MM/yyyy", example = "26/09/2023")
    @JsonFormat(pattern = "dd/MM/yyyy")
    val dateFounded: LocalDate,
    @field:NotBlank
    val countryCode: String,
    val vatNumber: String,
    val registrationNumber: String,
    val legalEntityType: LegalEntityType,
    val contactDetail: ContactDetailRequest,
)
