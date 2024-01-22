package io.billie.orders.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class Order(
    val id: UUID,
    val organizationId: UUID,
    val totalAmount: BigDecimal,
    val status: OrderStatus,
    val noOfTotalItems: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime? = null
)

enum class OrderStatus {
    PENDING, // The order has been created but not yet processed.
    PROCESSING, // The order is being processed.
    SHIPPED, //  All the items in the order has been shipped.
    COMPLETED, // All items in the order have been shipped, and the payment to merchant has been done
    CANCELLED // The order has been cancelled.
}