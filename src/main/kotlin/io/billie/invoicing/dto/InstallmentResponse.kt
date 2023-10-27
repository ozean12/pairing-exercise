package io.billie.invoicing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.invoicing.model.Invoice
import io.billie.invoicing.model.InvoiceStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

data class InstallmentResponse (
        @JsonProperty("installment_amount")
        @field:NotBlank val installmentAmount: BigDecimal,

        @JsonProperty("status")
        @field:NotBlank val status: InvoiceStatus,

        @JsonProperty("due_date")
        val dueDate: LocalDateTime
)
