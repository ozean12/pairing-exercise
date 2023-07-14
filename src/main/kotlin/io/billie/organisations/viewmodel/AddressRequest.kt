package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

class AddressRequest(
    @field:NotBlank @JsonProperty("address_line_1")
    val addressLine1: String,

    @JsonProperty("address_line_2")
    val addressLine2: String?,

    @field:NotBlank @JsonProperty("zip_code")
    val zipCode: String,

    @field:NotBlank @JsonProperty("city")
    val city: String,

    @field:NotBlank @JsonProperty("country_code")
    val countryCode: String
)
