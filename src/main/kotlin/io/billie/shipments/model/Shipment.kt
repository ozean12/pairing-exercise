package io.billie.shipments.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class Shipment(
    val id: UUID? = null,
    val orderId: UUID,
    val shipmentAmount: BigDecimal,
    val shippedAt: LocalDateTime
)