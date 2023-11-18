package io.billie.address.persistence

import io.billie.address.model.Country
import org.springframework.stereotype.Component

@Component
class CountryRdbmsPersistenceAdapter(private val repository: CountryRepository,
                                     private val mapper: CountryMapper
): CountryPersistenceAdapter {
    override fun getCountries(): List<Country> {
        return repository.findAll().map { mapper.entityToModel(it) }
    }

}
