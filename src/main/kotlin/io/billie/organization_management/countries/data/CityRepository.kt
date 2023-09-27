package io.billie.organization_management.countries.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CityRepository : JpaRepository<City, UUID> {

    fun findAllByCountryCodeIgnoreCase(countryCode: String): List<City>
    fun findByCountryCodeIgnoreCaseAndNameIgnoreCase(countryCode: String, name: String): City?
}
