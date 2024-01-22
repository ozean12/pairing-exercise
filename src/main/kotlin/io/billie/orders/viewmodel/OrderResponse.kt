package io.billie.orders.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

// TODO implement this while building functionality of notifying billie about the order invoice
data class OrderResponse(@JsonProperty("order_id") val orderId: UUID)
