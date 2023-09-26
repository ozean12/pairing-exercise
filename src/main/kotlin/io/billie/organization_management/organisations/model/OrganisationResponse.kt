package io.billie.organization_management.organisations.model

import com.fasterxml.jackson.annotation.JsonFormat
import io.billie.organization_management.countries.model.CountryResponse
import java.time.LocalDate
import java.util.*

data class OrganisationResponse(
    val id: UUID,
    val name: String,
    @JsonFormat(pattern = "dd/MM/yyyy")
    val dateFounded: LocalDate,
    val country: CountryResponse,
    val vatNumber: String?,
    val registrationNumber: String?,
    val legalEntityType: LegalEntityType,
    val contactDetail: ContactDetailResponse,
)
