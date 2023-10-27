package io.billie.orders.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class OrderRequest(
    @field:NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonProperty("created_time") val createdTime: LocalDateTime,
    @field:NotNull @JsonProperty("organisation_id") val organisationId: UUID,
    @field:NotBlank @JsonProperty("external_id") val externalId: String,
    @JsonProperty("state") val state: String, //TODO proper enum validator
    // TODO data fields needed for invoicing
)
