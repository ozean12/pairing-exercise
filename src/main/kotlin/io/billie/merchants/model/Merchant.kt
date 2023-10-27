package io.billie.merchants.model

import java.util.*
import javax.validation.constraints.NotBlank

data class Merchant(
        val id: UUID,

        @field:NotBlank val name: String,

        //TODO: more properties shall be added
)
