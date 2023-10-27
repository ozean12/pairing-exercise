package io.billie.orders.model

import io.billie.products.model.Product
import java.util.UUID

// TODO: related dao classes should be implemented along with association with order
data class OrderLine (
        val id: UUID,
        val quantity: Int,
        val product: Product
)
