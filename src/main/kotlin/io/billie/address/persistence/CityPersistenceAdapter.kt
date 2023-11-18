package io.billie.address.persistence

import io.billie.address.model.City

interface CityPersistenceAdapter {
    fun getCities(countryCode: String): List<City>
}
