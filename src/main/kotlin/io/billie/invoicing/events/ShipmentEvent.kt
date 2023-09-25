package io.billie.invoicing.events

import io.billie.customers.model.Customer
import io.billie.merchants.model.Merchant
import io.billie.orders.model.Order
import java.time.LocalDateTime

data class ShipmentEvent(
        val merchant: Merchant,
        val customer: Customer,
        val order: Order,
        val shipmentTimestamp: LocalDateTime
)
