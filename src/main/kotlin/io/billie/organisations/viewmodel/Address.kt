package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Address(
    val id: UUID?,
    val zipcode: String,
    @JsonProperty("street_name") val streetName: String,
    val number: Int,
    val city: String
)
