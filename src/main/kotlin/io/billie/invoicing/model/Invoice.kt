package io.billie.invoicing.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank

// TODO: To be further discussed in the meeting
data class Invoice (
        val id: UUID,

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

        val installments: MutableList<Installment>,

        @JsonProperty("date_created")
        val createdDate: LocalDateTime,

        @JsonProperty("last_update")
        val lastUpdate: LocalDateTime,

)