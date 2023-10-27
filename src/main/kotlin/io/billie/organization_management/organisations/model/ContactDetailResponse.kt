package io.billie.organization_management.organisations.model

import java.util.*

data class ContactDetailResponse(
    val id: UUID?,
    val phoneNumber: String?,
    val fax: String?,
    val email: String?
)
