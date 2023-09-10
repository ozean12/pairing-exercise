package io.billie.invoices.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CustomerOverview(
    val name: String,
    @JsonProperty("vat_number") val vatNumber: String,
    val address: String
)
