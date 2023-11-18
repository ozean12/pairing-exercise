package io.billie.address.service

import io.billie.address.model.Country

interface CountryService {
    fun getAll(): List<Country>
    fun getByCode(code: String): Country?
}
