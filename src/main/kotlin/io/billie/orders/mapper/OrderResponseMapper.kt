package io.billie.orders.mapper

import io.billie.orders.dto.OrderResponse
import io.billie.orders.model.Order


fun Order.toOrderResponse(): OrderResponse =
        OrderResponse(
                id = this.id,
                amount = this.amount,
                customerId = this.customerId
        )