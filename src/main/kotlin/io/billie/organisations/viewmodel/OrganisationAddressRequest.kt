package io.billie.organisations.viewmodel

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Table("ADDRESSES")
data class OrganisationAddressRequest(
    @Size(min = 36, max = 36) @JsonProperty("city_id") val cityId: UUID,
    @field:NotBlank @JsonProperty("pin_code") val pinCode: String,
    @field:NotBlank @JsonProperty("street_name") val streetName: String,
    @field:NotBlank @JsonProperty("plot_number") val plotNumber: String,
    val floor: String? = null,
    @JsonProperty("apartment_number") val apartmentNumber: String? = null,
)
