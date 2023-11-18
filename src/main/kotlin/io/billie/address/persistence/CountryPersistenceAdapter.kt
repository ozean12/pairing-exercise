package io.billie.address.persistence

import io.billie.address.model.Country

interface CountryPersistenceAdapter {
    fun getCountries(): List<Country>
}
