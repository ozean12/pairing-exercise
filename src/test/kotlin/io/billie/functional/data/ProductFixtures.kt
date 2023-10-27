package io.billie.functional.data

import io.billie.products.dto.ProductRequest
import java.math.BigDecimal


fun generateProductRequest(): ProductRequest =
        ProductRequest(
                name = "iphone",
                price = BigDecimal.valueOf(1299.00)
        )