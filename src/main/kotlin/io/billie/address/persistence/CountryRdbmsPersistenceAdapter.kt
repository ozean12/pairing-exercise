package io.billie.address.persistence

import io.billie.address.model.Country
import org.springframework.stereotype.Component

@Component
class CountryRdbmsPersistenceAdapter(private val repository: CountryRepository,
                                     private val mapper: CountryMapper
): CountryPersistenceAdapter {
    override fun getAll(): List<Country> {
        return repository.findAll().map { mapper.entityToModel(it)!! }
    }

    override fun getByCode(code: String): Country? {
        val entity = repository.findByCode(code);
        return mapper.entityToModel(entity)
    }

}
