package io.billie.organization_management.countries.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CountryRepository : JpaRepository<Country, UUID> {

    fun findByCode(code: String): Country?
}
