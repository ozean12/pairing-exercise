package io.billie.data.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(name = "payment_order")
@Entity
data class PaymentOrder (
    @Id
    var id: UUID,
    @NotNull
    var organisationId: UUID,
    var itemsCount: Int,
    var totalPrice: Double,
    var remainingAmount: Double,
    var currencyCode: String,
    var createdAt: OffsetDateTime,
    var updatedAt: OffsetDateTime
)