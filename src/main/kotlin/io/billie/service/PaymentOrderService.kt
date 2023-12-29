package io.billie.service

import io.billie.data.entity.PaymentOrder
import io.billie.dto.CreatePaymentOrderResponseDTO
import io.billie.dto.PaymentOrderCreateRequestDTO
import io.billie.dto.PaymentOrderStatusResponseDTO
import io.billie.dto.ProcessPaymentDTO
import java.util.Optional
import java.util.UUID

interface PaymentOrderService {

    fun createPaymentOrder(paymentOrderCreateRequest: PaymentOrderCreateRequestDTO) : CreatePaymentOrderResponseDTO

    fun getPaymentOrderById(paymentOrderId: UUID) : Optional<PaymentOrder>

    fun processPayment(processPaymentDTO: ProcessPaymentDTO)

    fun getPaymentOrderStatus(paymentOrderId: UUID): PaymentOrderStatusResponseDTO
}
