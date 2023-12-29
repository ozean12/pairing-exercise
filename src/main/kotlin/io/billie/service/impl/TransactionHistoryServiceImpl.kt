package io.billie.service.impl

import io.billie.data.entity.TransactionHistory
import io.billie.data.repository.TransactionHistoryRepository
import io.billie.service.TransactionHistoryService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionHistoryServiceImpl(private val transactionHistoryRepository: TransactionHistoryRepository):TransactionHistoryService {
    override fun storePayment(transactionHistory: TransactionHistory) {
        this.transactionHistoryRepository.save(transactionHistory)
    }

    override fun getPaymentsByPaymentOrderId(paymentOrderId: UUID): List<TransactionHistory> {
        return this.transactionHistoryRepository.findAllByPaymentOrderId(paymentOrderId)
    }
}