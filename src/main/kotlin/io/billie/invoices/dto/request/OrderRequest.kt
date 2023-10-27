package io.billie.invoices.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoices.dto.CustomerOverview
import io.billie.invoices.model.Product
import java.util.*

data class OrderRequest(
    @JsonProperty("order_id") val orderId: UUID,
    @JsonProperty("total_gross") val totalGross: Double,
    @JsonProperty("organisation_id") val organisationId: UUID,
    val customer: CustomerOverview,
    val products: List<Product>
)