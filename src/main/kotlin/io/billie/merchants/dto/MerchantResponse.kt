package io.billie.merchants.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.organisations.dto.OrganisationResponse
import java.util.*
import javax.validation.constraints.NotBlank

data class MerchantResponse (
        val id: UUID,
        @field:NotBlank val name: String
)