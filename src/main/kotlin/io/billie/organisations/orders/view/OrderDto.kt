package io.billie.organisations.orders.view

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.organisations.orders.domain.Order
import java.math.BigDecimal
import java.util.UUID

/**
 * Data transfer object mapped to [Order] domain object and exposed to external service
 */
data class OrderDto (
        @JsonProperty("order_id") val id: UUID,
        @JsonProperty("organisation_id") val organisationId: UUID,
        @JsonProperty("amount") val amount: BigDecimal
)