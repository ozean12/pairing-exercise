package io.billie.orders.model

import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.NotBlank

data class Order(
        val id: UUID,

        @field:NotBlank val amount: BigDecimal,

        // TODO: to be replaced with Customer entity
        @field:NotBlank val customerId: UUID

        //TODO: to be implemented later during the session
)