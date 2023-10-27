package io.billie.orders.dto

import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.NotBlank

data class OrderRequest(
        @field:NotBlank val amount: BigDecimal,

        @field:NotBlank val customerId: UUID
)
