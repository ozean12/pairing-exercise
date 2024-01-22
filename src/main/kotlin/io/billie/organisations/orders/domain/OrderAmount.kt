package io.billie.organisations.orders.domain

import java.math.BigDecimal

/**
 * Domain object represents order amount, should always be positive
 */
data class OrderAmount(val amount: BigDecimal) {
    init {
        require(amount.compareTo(BigDecimal.ZERO) > 0) { "Order amount must be positive" }
    }
}
