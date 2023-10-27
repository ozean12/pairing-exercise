package io.billie.customers.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class CustomerRequest (
        @field:NotBlank val name: String,
        @field:NotBlank @JsonProperty("address_details")
        val address: String
)