package io.billie.merchants.dto

import javax.validation.constraints.NotBlank

data class MerchantRequest (
        @field:NotBlank val name: String,
)