package io.billie.shipments.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class ShipmentResponse(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("shipped_amount") val shipmentAmount: BigDecimal,
    @JsonProperty("shipped_at") val shippedAt: LocalDateTime
)