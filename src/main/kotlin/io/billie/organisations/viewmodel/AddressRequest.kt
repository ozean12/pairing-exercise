package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import java.util.UUID



data class AddressRequest(
        @field:NotBlank val city: String,
        @field:NotBlank val postcode: String,
        @field:NotBlank @JsonProperty("address_line_1") val addressLine1: String,
        @JsonProperty("address_line_2") val addressLine2: String?,
        @field:JsonProperty("organisation_id") val organisationId: UUID,
        @field:JsonProperty("city_id") val cityId: UUID

)