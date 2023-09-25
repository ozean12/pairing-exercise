package io.billie.products.dto

import java.util.*
import javax.validation.constraints.NotBlank

data class ProductResponse (
        val id: UUID,
        @field:NotBlank val name: String,
        @field:NotBlank val price: Double
)