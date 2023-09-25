package io.billie.customers.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotBlank

data class Customer (
        val id: UUID,

        @field:NotBlank val name: String,

        // TODO: shall be replaced with AddressDetail later
        @field:NotBlank @JsonProperty("address_details") val address: String
)
