package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Address(
    val id: UUID,
    val line1: String,
    val line2: String?,
    @JsonProperty("post_code") val postCode: String,
    val city: String,
    @JsonProperty("country_code") val countryCode: String
)