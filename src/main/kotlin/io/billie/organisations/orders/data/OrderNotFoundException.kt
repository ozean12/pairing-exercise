package io.billie.organisations.orders.data

import io.billie.organisations.orders.domain.OrderId

/**
 * Error condition representing non-existence of order
 */
class OrderNotFoundException(orderId: OrderId) : RuntimeException("No order found with id " + orderId.id)