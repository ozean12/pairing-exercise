package io.billie.countries.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.ResultSet
import java.util.*
import javax.validation.constraints.Size

data class CityResponse(
    val id: UUID,
    val name: String,
    @Size(min = 2, max = 2)
    @JsonProperty("country_code")
    val countryCode: String
)

fun toCityResponse(resultSet: ResultSet): CityResponse =
    CityResponse(
        resultSet.getObject("city_id", UUID::class.java),
        resultSet.getString("name"),
        resultSet.getString("country_code")
    )