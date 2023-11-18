package io.billie.organisations.service.model

import io.billie.address.model.Country
import io.billie.organisations.model.LegalEntityType
import java.time.LocalDate
import java.util.*

data class Organization (
    val id: UUID?,
    val name: String,
    val foundedDate: LocalDate,
    val vatNumber: String?,
    val registrationNumber: String?,
    val legalEntityType: LegalEntityType,
    val contactDetails: ContactDetails,
    var country: Country?
)
