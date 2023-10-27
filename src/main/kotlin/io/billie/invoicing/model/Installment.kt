package io.billie.invoicing.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

data class Installment (
        val id: UUID,

        @JsonProperty("invoice_uid")
        val invoice: Invoice,

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