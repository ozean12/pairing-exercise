package io.billie.countries.data

import io.billie.countries.model.CityResponse
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.util.*

@Repository
class CityRepository(
    private val jdbcTemplate: JdbcTemplate,
) {

    @Transactional(readOnly = true)
    fun findByCountryCode(countryCode: String): List<CityResponse> {
        return jdbcTemplate.query(
            "select id, name, country_code from organisations_schema.cities where country_code = ?",
            cityResponseMapper(),
            countryCode
        )
    }

    fun existsById(cityId: UUID): Boolean {
        val exists = jdbcTemplate.query(
            "SELECT EXISTS(SELECT 1 FROM organisations_schema.cities WHERE id = ?)",
            ResultSetExtractor {
                it.next()
                it.getBoolean(1)
            },
            cityId,
        )
        return exists != null && exists
    }

    private fun cityResponseMapper() = RowMapper<CityResponse> { it: ResultSet, _: Int ->
        CityResponse(
            it.getObject("id", UUID::class.java),
            it.getString("name"),
            it.getString("country_code")
        )
    }
}
