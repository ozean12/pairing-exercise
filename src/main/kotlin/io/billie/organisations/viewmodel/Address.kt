package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Address(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("country_code") val countryCode: String,
    @JsonProperty("city_name") val cityName: String,
    @JsonProperty("zip_code") val zipCode: String,
    @JsonProperty("street") val street: String,
    @JsonProperty("house_number") val houseNumber: String,
    @JsonProperty("comment") val comment: String,
)

data class AddressCreateRequest(
    @JsonProperty("country_code") val countryCode: String,
    @JsonProperty("city_name") val cityName: String,
    @JsonProperty("zip_code") val zipCode: String,
    @JsonProperty("street") val street: String,
    @JsonProperty("house_number") val houseNumber: String,
    @JsonProperty("comment") val comment: String,
)
