package io.billie.organisations.orders.view

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.organisations.orders.domain.Shipment
import java.math.BigDecimal

/**
 * Request object mapped to [Shipment] domain object
 */
data class ShipmentRequest(@JsonProperty("amount") val amount: BigDecimal)