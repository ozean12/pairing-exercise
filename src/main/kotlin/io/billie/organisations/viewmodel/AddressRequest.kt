package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AddressRequest(
    @JsonProperty("street_name") val street_name: String?,
    val home_number: String?,
    val zip_code: String?
)
