package io.billie.invoices.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

// prepared for future interactions
@Table("invoices")
data class Invoice(
    val id: UUID,
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("is_paid") val isPaid: Boolean
)
