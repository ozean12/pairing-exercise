package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Address(
    val id: UUID,
    @JsonProperty("city_id") val cityId: String,
    @JsonProperty("zip_code") val zipCode: String,
    val street: String,
    @JsonProperty("street_number") val streetNumber: String,
    @JsonProperty("apartment_number") val apartmentNumber: String?,
)