package io.billie.organisations.orders.domain

import io.billie.organisations.viewmodel.Entity
import org.springframework.data.relational.core.mapping.Table

/**
 * Domain object represents order for organisation
 */
@Table("ORDERS")
data class Order (

        val orderId: OrderId?,
        val organisationId: Entity,
        val orderAmount: OrderAmount
)