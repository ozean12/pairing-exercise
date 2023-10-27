package io.billie.invoicing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

data class InstallmentRequest(
        @JsonProperty("order_uid")
        @field:NotBlank val orderUid: UUID,

        @JsonProperty("installment_amount")
        @field:NotBlank val installmentAmount: BigDecimal,

        @JsonProperty("status")
        @field:NotBlank val status: InvoiceStatus,

        @JsonProperty("customer_uid")
        @field:NotBlank val customerUid: UUID,

        @JsonProperty("merchant_uid")
        @field:NotBlank val merchantUid: UUID,

        @JsonProperty("date_created")
        val createdDate: LocalDateTime,

        @JsonProperty("last_update")
        val lastUpdate: LocalDateTime,

        @JsonProperty("due_date")
        val dueDate: LocalDateTime
)
