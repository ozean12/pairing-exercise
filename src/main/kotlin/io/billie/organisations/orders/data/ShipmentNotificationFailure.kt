package io.billie.organisations.orders.data

import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.orders.domain.ShipmentAmount


/**
 * Error condition representing shipment notification failure
 */
class ShipmentNotificationFailure(shipmentAmount: ShipmentAmount, orderId: OrderId)
    : RuntimeException("Shipment notification for amount ${shipmentAmount.amount} failed for order id ${orderId.id}")