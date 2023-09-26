package io.billie.organization_management.countries.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CityRepository : JpaRepository<City, UUID> {

    fun findAllByCountryCode(countryCode: String): List<City>
}
