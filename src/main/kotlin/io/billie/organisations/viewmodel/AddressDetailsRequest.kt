package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class AddressDetailsRequest(
    @JsonProperty("country_code") val countryCode: String?,
    val state: String?,
    val city: String?,
    @JsonProperty("zip_code") val zipCode: String?,
    val street: String?,
)
