package io.billie.orders.service

import io.billie.orders.dto.OrderRequest
import io.billie.orders.model.Order
import java.util.*

interface OrderService {
    fun createOrder(orderRequest: OrderRequest): UUID
    fun findOrders(): List<Order>
    fun findOrderByUid (orderUid: UUID): Order
}