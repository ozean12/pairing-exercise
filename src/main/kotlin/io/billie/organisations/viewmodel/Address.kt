package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotBlank

data class Address(
    val id: UUID?,
    @field:NotBlank val city: String,
    @field:NotBlank @JsonProperty("postal_code") val postalCode: String,
    @field:NotBlank @JsonProperty("street_and_number") val streetAndNumber: String,
)