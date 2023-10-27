package io.billie.organisations.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContactDetails(
    val id: UUID?,
    @JsonProperty("phone_number") val phoneNumber: String?,
    val fax: String?,
    val email: String?
)
