package io.billie.shipments.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Positive

data class ShipmentRequest(
    @JsonProperty("order_id") val orderId: UUID,
    @field:Positive @JsonProperty("shipment_amount") val shipmentAmount: BigDecimal,
    @field:PastOrPresent @JsonProperty("shipped_at") val shippedAt: LocalDateTime
)