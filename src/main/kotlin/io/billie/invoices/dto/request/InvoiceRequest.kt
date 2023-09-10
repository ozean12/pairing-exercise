package io.billie.invoices.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class InvoiceRequest(
    @JsonProperty("order_id") val orderId: UUID
)