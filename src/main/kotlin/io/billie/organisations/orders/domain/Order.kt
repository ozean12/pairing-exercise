package io.billie.organisations.orders.domain

import io.billie.organisations.orders.data.ShipmentAmountExceedOrderTotalException
import io.billie.organisations.viewmodel.Entity
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

/**
 * Domain object represents order for organisation
 */
@Table("ORDERS")
data class Order (
        val orderId: OrderId?,
        val organisationId: Entity,
        val orderAmount: OrderAmount,
        val shipments: MutableSet<Shipment> ) {

    fun addShipment(shipment: Shipment) {
        var existingShipmentTotal = BigDecimal.ZERO
        for (existingShipment in shipments){
            existingShipmentTotal = existingShipmentTotal.add(existingShipment.shipmentAmount.amount)
        }

        val shipmentTotal = existingShipmentTotal.add(shipment.shipmentAmount.amount)
        if (shipmentTotal.compareTo(orderAmount.amount) > 0)
            throw ShipmentAmountExceedOrderTotalException(ShipmentAmount(shipmentTotal), orderId!!)

        shipments.add(shipment)
    }
}