package io.billie.address.persistence

import io.billie.address.persistence.model.CityEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CityRepository: CrudRepository<CityEntity, UUID>{
    fun findByCountryCode(countryCode: String): List<CityEntity>
}
