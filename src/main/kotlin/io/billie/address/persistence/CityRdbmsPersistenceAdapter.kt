package io.billie.address.persistence

import io.billie.address.model.City
import org.springframework.stereotype.Component

@Component
class CityRdbmsPersistenceAdapter(
    private val repository: CityRepository,
    private val mapper: CityMapper
): CityPersistenceAdapter {
    override fun getCities(countryCode: String): List<City> {
        return repository.findByCountryCode(countryCode).map { mapper.entityToModel(it) }
    }

}
