package io.billie.invoices.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OrganisationOverview(
    val name: String,
    @JsonProperty("vat_number") val vatNumber: String,
    //TODO add address: val address: String
)
