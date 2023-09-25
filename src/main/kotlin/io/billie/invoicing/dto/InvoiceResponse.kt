package io.billie.invoicing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoicing.model.Installment
import io.billie.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

data class InvoiceResponse (
        val id: UUID,

        @JsonProperty("order_uid")
        @field:NotBlank val orderUid: UUID,

        @JsonProperty("order_amount")
        @field:NotBlank val orderAmount: BigDecimal,

        @JsonProperty("overall_status")
        @field:NotBlank val status: InvoiceStatus,

        @JsonProperty("customer_uid")
        @field:NotBlank val customerUid: UUID, //TODO: to be replaced with Customer

        @JsonProperty("merchant_uid")
        @field:NotBlank val merchantUid: UUID, //TODO: to be replaced with Merchant

        val installments: List<InstallmentResponse>
)