package io.billie.invoices.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID

/**
 * Invoice view model that is not stored in persistence,
 * but constructed based on combining multiple entities
 */
data class FullInvoice(
    val id: UUID,
    @JsonProperty("is_paid") val isPaid: Boolean,
    @JsonProperty("invoice_timestamp") val invoiceTimestamp: LocalDateTime,
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("organisation_name") val organisationName: String,
    @JsonProperty("organisation_VAT_number") val organisationVATNumber: String,
    @JsonProperty("customer_name") val customerName: String,
    @JsonProperty("customer_VAT_number") val customerVATNumber: String,
    @JsonProperty("customer_address") val customerAddress: String,
    @JsonProperty("total_gross") val totalGross: Double,
    val products: List<Product>
)
