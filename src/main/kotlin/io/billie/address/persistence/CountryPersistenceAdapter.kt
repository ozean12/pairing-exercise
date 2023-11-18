package io.billie.address.persistence

import io.billie.address.model.Country

interface CountryPersistenceAdapter {
    fun getAll(): List<Country>
    fun getByCode(code: String): Country?
}
