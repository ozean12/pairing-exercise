package io.billie.data.repository

import io.billie.data.entity.TransactionHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionHistoryRepository: JpaRepository<TransactionHistory, UUID> {

    fun findAllByPaymentOrderId(paymentOrderId: UUID): List<TransactionHistory>
}
