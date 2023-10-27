package io.billie.products.mapper

import io.billie.products.dto.ProductResponse
import io.billie.products.model.Product

fun Product.toProductResponse(): ProductResponse =
        ProductResponse(
                id = this.id,
                name = this.name,
                price = this.price
        )