package io.billie.service

import io.billie.data.entity.TransactionHistory
import java.util.UUID

interface TransactionHistoryService {
    fun storePayment(transactionHistory: TransactionHistory)
    fun getPaymentsByPaymentOrderId(paymentOrderId: UUID): List<TransactionHistory>
}
