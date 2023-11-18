package io.billie.address.service

import io.billie.address.model.City
import io.billie.address.persistence.CityPersistenceAdapter
import org.springframework.stereotype.Service

@Service
class SimpleCityService(private val persistenceAdapter: CityPersistenceAdapter): CityService {
    override fun getCities(countryCode: String): List<City> {
        return persistenceAdapter.getCities(countryCode);
    }
}
