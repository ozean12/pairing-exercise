package io.billie.organization_management.organisations.model

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class AddressRequest(
    @field:NotNull
    val cityId: UUID,
    @field:NotEmpty
    val street: String,
    @field:NotEmpty
    val number: String,
    @field:NotEmpty
    val postalCode: String,
)
