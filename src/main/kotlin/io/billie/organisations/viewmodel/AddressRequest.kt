package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AddressRequest(
    @field:NotBlank val line1: String,
    val line2: String?,
    @field:NotBlank @JsonProperty("post_code") val postCode: String,
    @field:NotBlank val city: String,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String
)
