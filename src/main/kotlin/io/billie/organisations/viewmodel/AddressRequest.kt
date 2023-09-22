package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AddressRequest(
    @field:NotBlank val address1: String,
    val address2: String,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String,
    @field:NotBlank val city: String,
    val state: String,
    @field:NotBlank @JsonProperty("postal_code") val postalCode: String
)