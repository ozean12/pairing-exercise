package io.billie.orders.dto

import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.NotBlank

data class OrderResponse (
        val id: UUID,

        @field:NotBlank val amount: BigDecimal,

        // TODO: to be replaced with Customer entity
        @field:NotBlank val customerId: UUID
)