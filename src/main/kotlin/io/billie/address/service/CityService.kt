package io.billie.address.service

import io.billie.address.model.City

interface CityService {
    fun getCities(countryCode: String): List<City>
}
