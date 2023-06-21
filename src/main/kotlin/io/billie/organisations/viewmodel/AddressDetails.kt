package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.countries.model.CityResponse
import io.billie.countries.model.CountryResponse
import java.sql.ResultSet
import java.util.*

data class AddressDetails (
    val id: UUID,
    @JsonProperty("address_string") val addressString: String,
    @JsonProperty("zip_code") val zipCode: String,
    val country: CountryResponse,
    val city: CityResponse,
)

fun toAddressDetails(resultSet: ResultSet, country: CountryResponse, city: CityResponse) =
    AddressDetails(
        id = resultSet.getObject("address_details_id", UUID::class.java),
        addressString = resultSet.getString("address_string"),
        zipCode = resultSet.getString("zip_code"),
        country = country,
        city = city
    )