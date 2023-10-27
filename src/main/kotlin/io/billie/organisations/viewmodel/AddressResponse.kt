package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotBlank

data class AddressResponse(
    @field:NotBlank val id: UUID,
    @field:NotBlank val address1: String,
    val address2: String,
    @field:NotBlank @JsonProperty("country_code") val countryCode: String,
    @field:NotBlank val city: String,
    @field:NotBlank val state: String,
    @field:NotBlank @JsonProperty("postal_code")  val postalCode: String
)