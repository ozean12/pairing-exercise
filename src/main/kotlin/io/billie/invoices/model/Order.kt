package io.billie.invoices.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table("orders")
data class Order(
    @Id @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("order_timestamp") val orderTimestamp: LocalDateTime,
    @JsonProperty("organisation_id") val organisationId: UUID,
    @JsonProperty("customer_name") val customerName: String,
    @JsonProperty("customer_VAT_number") val customerVATNumber: String,
    @JsonProperty("customer_address") val customerAddress: String,
    @JsonProperty("total_gross") val totalGross: Double,
    val products: List<Product>
)