package io.billie.products.dto

import java.math.BigDecimal
import javax.validation.constraints.NotBlank

data class ProductRequest (
        @field:NotBlank val name: String,
        @field:NotBlank val price: BigDecimal
)