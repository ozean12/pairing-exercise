package io.billie.functional.data

import io.billie.orders.dto.OrderRequest
import java.math.BigDecimal
import java.util.UUID

fun generateOrderRequest(customerId: UUID, amount: BigDecimal): OrderRequest =
        OrderRequest(
            amount = amount, customerId = customerId
        )