package io.billie.organisations.orders.data

import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.orders.domain.ShipmentAmount


/**
 * Error condition representing total shipment amount exceeding order total amount
 */
class ShipmentAmountExceedOrderTotalException(shipmentAmount: ShipmentAmount, orderId: OrderId)
    : RuntimeException("Shipments total ${shipmentAmount.amount} exceed order amount for order id ${orderId.id}")