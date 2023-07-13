package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.countries.model.CountryResponse
import java.util.UUID

class AddressResponse(
    @JsonProperty("id")
    val id: UUID?,

    @JsonProperty("address_line_1")
    val addressLine1: String,

    @JsonProperty("address_line_2")
    val addressLine2: String?,

    @JsonProperty("zip_code")
    val zipCode: String,

    @JsonProperty("city")
    val city: String,

    @JsonProperty("country")
    val country: CountryResponse
)
