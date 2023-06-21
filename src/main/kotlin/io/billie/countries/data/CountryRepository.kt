package io.billie.countries.data

import io.billie.countries.model.CountryResponse
import io.billie.countries.model.toCountryResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
class CountryRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Transactional(readOnly = true)
    fun findCountries(): List<CountryResponse> = jdbcTemplate.query(
        "select id as country_id, name, country_code from organisations_schema.countries",
        countryResponseMapper()
    )

    @Transactional(readOnly = true)
    fun isCountryCodeValid(countryCode: String): Boolean =
        jdbcTemplate.query(
            "select count(country_code) from organisations_schema.countries c WHERE c.country_code = ?",
            ResultSetExtractor {
                it.next()
                it.getInt(1)
            },
            countryCode
        )?.let { it > 0 } ?: false


    private fun countryResponseMapper() = RowMapper<CountryResponse> { it: ResultSet, _: Int ->
        toCountryResponse(it)
    }
}
