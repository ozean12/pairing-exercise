package io.billie.products.model

import java.util.*
import javax.validation.constraints.NotBlank

data class Product(
        val id: UUID,
        @field:NotBlank val name: String,
        @field:NotBlank val price: Double
)

