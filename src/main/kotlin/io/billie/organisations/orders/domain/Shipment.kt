package io.billie.organisations.orders.domain

import org.springframework.data.relational.core.mapping.Table

/**
 * Domain object represents shipments for an [Order]
 */
@Table("SHIPMENTS")
data class Shipment(
    val shipmentId: ShipmentId?,
    val orderId: OrderId,
    val shipmentAmount: ShipmentAmount
)
