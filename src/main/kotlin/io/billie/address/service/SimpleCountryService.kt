package io.billie.address.service

import io.billie.address.model.Country
import io.billie.address.persistence.CountryPersistenceAdapter
import org.springframework.stereotype.Service

@Service
class SimpleCountryService(private val persistenceAdapter: CountryPersistenceAdapter): CountryService {
    override fun getCountries(): List<Country> {
        return persistenceAdapter.getCountries()
    }
}
