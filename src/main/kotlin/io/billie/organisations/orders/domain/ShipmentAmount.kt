package io.billie.organisations.orders.domain

import java.math.BigDecimal

/**
 * Domain object represents shipment amount, should always be positive
 */
data class ShipmentAmount(val amount: BigDecimal) {
    init {
        require(amount.compareTo(BigDecimal.ZERO) > 0) { "Shipment amount must be positive" }
    }
}