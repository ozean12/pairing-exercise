package io.billie.data.entity

import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "transaction_history")
@Entity
data class TransactionHistory(
    @Id
    val id: UUID,
    val paymentOrderId: UUID,
    val amountBeforeTransaction: Double,
    val deductedAmount: Double,
    val remainingAmount: Double,
    val createdAt: OffsetDateTime
)
