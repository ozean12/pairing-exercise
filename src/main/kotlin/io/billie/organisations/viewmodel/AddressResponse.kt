package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AddressResponse(
        val city: String?,
        val postcode: String?,
        @JsonProperty("address_line_1") val addressLine1: String?,
        @JsonProperty("address_line_2") val addressLine2: String?
)