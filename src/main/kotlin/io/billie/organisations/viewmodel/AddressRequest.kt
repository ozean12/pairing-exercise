package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty

data class AddressRequest(
    val zipcode: String,
    @JsonProperty("street_name") val streetName: String,
    val number: Int,
    val city: String
)
