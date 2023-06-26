package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class AddressResponse(
    val id: UUID?,
    @JsonProperty("street_name") val streetName: String,
    @JsonProperty("house_number") val houseNumber: String,
    val city: String,
    @JsonProperty("additional_address") val additionalAddress: String?,
    @JsonProperty("pin_code") val pinCode: String,
    val country: String
)