package io.billie.organisations.model

import io.billie.countries.model.CityResponse
import io.billie.countries.model.CountryResponse
import java.util.UUID

// TODO: To be implemented later
data class AddressDetails(
        val id: UUID,
        val countryResponse: CountryResponse,
        val cityResponse: CityResponse,
        val zipCode: String,
        val addressLine1: String,
        val addressLine2: String
)
