package io.billie.customers.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotBlank

data class CustomerResponse (
        val id: UUID,

        @field:NotBlank val name: String,

        @field:NotBlank @JsonProperty("address_details")
        val address: String
)