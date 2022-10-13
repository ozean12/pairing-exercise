package io.billie.countries.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.Size

data class CountryResponse(
    val id: UUID,
    val name: String,
    @JsonProperty("country_code") @Size(min = 2, max = 2) val countryCode: String,
)
