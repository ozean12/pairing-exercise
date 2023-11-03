package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty

data class AddressRequest(
    @JsonProperty("country_code") val countryCode: String,
    val city: String,
    val street: String,
    @JsonProperty("house_number") val houseNumber: String,
    @JsonProperty("postal_code") val postalCode: String,
    @JsonProperty("additional_info") val additionalInfo: String?
)
