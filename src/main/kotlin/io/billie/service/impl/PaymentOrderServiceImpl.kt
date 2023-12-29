package io.billie.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.data.entity.PaymentOrder
import io.billie.data.entity.TransactionHistory
import io.billie.data.repository.PaymentOrderRepository
import io.billie.dto.CreatePaymentOrderResponseDTO
import io.billie.dto.PaymentOrderCreateRequestDTO
import io.billie.dto.PaymentOrderStatusResponseDTO
import io.billie.dto.ProcessPaymentDTO
import io.billie.exception.BadRequestException
import io.billie.exception.NotFoundException

import io.billie.service.PaymentOrderService
import io.billie.service.OrganisationService
import io.billie.service.PaymentService
import io.billie.service.TransactionHistoryService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class PaymentOrderServiceImpl(
    private val paymentOrderRepository: PaymentOrderRepository,
    private val organizationService: OrganisationService,
    private val transactionHistoryService: TransactionHistoryService,
    private val objectMapper: ObjectMapper,
    private val paymentService: PaymentService
): PaymentOrderService {

    override fun createPaymentOrder(paymentOrderCreateRequest: PaymentOrderCreateRequestDTO): CreatePaymentOrderResponseDTO {
        val organisation = this.organizationService.getOrganisationById(paymentOrderCreateRequest.organisationId)
        if(organisation.isEmpty) {
            throw NotFoundException("Organization is not found by provided ID: " + paymentOrderCreateRequest.organisationId)
        }

        val storedOrder = this.paymentOrderRepository.save(objectMapper.convertValue(paymentOrderCreateRequest, PaymentOrder::class.java))
        this.paymentService.holdMoney(paymentOrderCreateRequest.cardDetails)


        return CreatePaymentOrderResponseDTO(id = storedOrder.id)
    }

    override fun getPaymentOrderById(paymentOrderId: UUID): Optional<PaymentOrder> {
        return this.paymentOrderRepository.findById(paymentOrderId)
    }

    override fun processPayment(processPaymentDTO: ProcessPaymentDTO) {
        val mayBePaymentOrder = this.getPaymentOrderById(processPaymentDTO.paymentOrderId)
        if (mayBePaymentOrder.isEmpty) {
            throw NotFoundException("Payment order is not found by provided ID: " + processPaymentDTO.paymentOrderId)
        }

        val paymentOrder = mayBePaymentOrder.get()
        if (processPaymentDTO.amountToPay <= 0
            || processPaymentDTO.amountToPay > paymentOrder.totalPrice
            || processPaymentDTO.amountToPay > paymentOrder.remainingAmount
            || (paymentOrder.remainingAmount -  processPaymentDTO.amountToPay) < 0
            ) {
            throw BadRequestException("Amount to pay is less than 0 or is greater than remaining amount or remaining amount is less than 0.")
        }

        val amountBeforeTransaction = paymentOrder.remainingAmount;
        paymentOrder.updatedAt = OffsetDateTime.now()
        paymentOrder.remainingAmount -=  processPaymentDTO.amountToPay

        val storedPaymentOrder = this.paymentOrderRepository.save(paymentOrder)

        this.transactionHistoryService.storePayment(TransactionHistory(
            UUID.randomUUID(),
            storedPaymentOrder.id,
            amountBeforeTransaction,
            processPaymentDTO.amountToPay,
            storedPaymentOrder.remainingAmount,
            OffsetDateTime.now()
            ))

        this.paymentService.withdrawMoney(processPaymentDTO.paymentOrderId, processPaymentDTO.amountToPay)
    }

    override fun getPaymentOrderStatus(paymentOrderId: UUID): PaymentOrderStatusResponseDTO {

        val paymentOrder = this.getPaymentOrderById(paymentOrderId)
        if (paymentOrder.isEmpty) {
            throw NotFoundException("Payment order is not found by provided ID: " + paymentOrderId)
        }

        val status = if (paymentOrder.get().remainingAmount > 0) {
            "pending"
        } else {
            "completed"
        }

        return PaymentOrderStatusResponseDTO(status = status)
    }
}