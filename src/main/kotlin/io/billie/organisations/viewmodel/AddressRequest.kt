package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AddressRequest(
    @field:NotBlank  @JsonProperty("city_id") val cityId: String,
    @field:Size(max = 10) @field:NotBlank @JsonProperty("zip_code") val zipCode: String,
    @field:Size(max = 50) @field:NotBlank val street: String,
    @field:Size(max = 10) @field:NotBlank @JsonProperty("street_number") val streetNumber: String,
    @field:Size(max = 5) @JsonProperty("apartment_number") val apartmentNumber: String? = null,
)
