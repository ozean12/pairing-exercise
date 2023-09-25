package io.billie.invoicing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoicing.model.Installment
import io.billie.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

data class InvoiceRequest(
        @JsonProperty("order_uid")
        @field:NotBlank val orderUid: UUID,

        @JsonProperty("order_amount")
        @field:NotBlank val orderAmount: BigDecimal,

        @JsonProperty("overall_status")
        @field:NotBlank val status: InvoiceStatus,

        @JsonProperty("customer_uid")
        @field:NotBlank val customerUid: UUID,

        @JsonProperty("merchant_uid")
        @field:NotBlank val merchantUid: UUID,

        val installmentRequests: MutableList<InstallmentRequest>,

        @JsonProperty("date_created")
        val createdDate: LocalDateTime,

        @JsonProperty("last_update")
        val lastUpdate: LocalDateTime
)
