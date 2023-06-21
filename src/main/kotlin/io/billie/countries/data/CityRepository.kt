package io.billie.countries.data

import io.billie.countries.model.CityResponse
import io.billie.countries.model.toCityResponse
import io.billie.organisations.data.UnableToFindCity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class CityRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findByCountryCode(countryCode: String): List<CityResponse> {
        return jdbcTemplate.query(
            "select id as city_id, name, country_code from organisations_schema.cities where country_code = ?",
            cityResponseMapper(),
            countryCode
        )
    }

    @Transactional(readOnly = true)
    fun isCityIdValid(cityId: String): Boolean = try {
        jdbcTemplate.query(
            "select count(id) from organisations_schema.cities c WHERE c.id = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            UUID.fromString(cityId)
        )?.let { it > 0 } ?: false
    } catch (e: IllegalArgumentException) {
        throw UnableToFindCity(cityId)
    }

    private fun cityResponseMapper() = RowMapper<CityResponse> { it: ResultSet, _: Int ->
        toCityResponse(it)
    }
}
