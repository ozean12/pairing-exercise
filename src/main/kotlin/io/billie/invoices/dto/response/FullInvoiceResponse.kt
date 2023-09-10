package io.billie.invoices.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoices.dto.CustomerOverview
import io.billie.invoices.dto.OrganisationOverview
import io.billie.invoices.model.Product
import java.util.UUID

data class FullInvoiceResponse(
    val id: UUID,
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("is_paid") val isPaid: Boolean,
    @JsonProperty("issue_date") val formattedTime: String,
    val organisation: OrganisationOverview,
    val customer: CustomerOverview,
    @JsonProperty("total_gross") val totalGross: Double,
    val products: List<Product>
)