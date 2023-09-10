package io.billie.invoices.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoices.dto.CustomerOverview
import io.billie.invoices.model.Product
import java.time.LocalDateTime
import java.util.*

data class OrderResponse(
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("order_timestamp") val orderTimestamp: LocalDateTime,
    @JsonProperty("total_gross") val totalGross: Double,
    @JsonProperty("organisation_id") val organisationId: UUID,
    val customer: CustomerOverview,
    val products: List<Product>
)