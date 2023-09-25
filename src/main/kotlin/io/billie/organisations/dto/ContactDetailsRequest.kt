package io.billie.organisations.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ContactDetailsRequest(
    @JsonProperty("phone_number") val phoneNumber: String?,
    val fax: String?,
    val email: String?
)
