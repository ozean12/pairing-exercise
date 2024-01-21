package io.billie.organisations.orders.view

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.organisations.orders.domain.Order
import java.math.BigDecimal

/**
 * Request object mapped to [Order] domain object
 */
data class OrderRequest(@JsonProperty("amount") val amount: BigDecimal)