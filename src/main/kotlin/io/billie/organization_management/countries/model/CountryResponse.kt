package io.billie.organization_management.countries.model

import jakarta.validation.constraints.Size
import java.util.*

data class CountryResponse(
    val id: UUID,
    val name: String,
    @Size(min = 2, max = 2)
    val countryCode: String,
)
