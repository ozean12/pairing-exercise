package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AddressDetailsRequest (
    @field:NotBlank @JsonProperty("address_string") val addressString: String,
    @field:NotBlank @JsonProperty("zip_code") val zipCode: String,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String,
    @field:NotBlank @JsonProperty("city_id") val cityId: String,
)