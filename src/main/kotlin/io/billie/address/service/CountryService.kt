package io.billie.address.service

import io.billie.address.model.Country

interface CountryService {
    fun getCountries(): List<Country>
}
