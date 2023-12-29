package io.billie.resource

import io.billie.dto.*
import io.billie.service.PaymentOrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Closeable
import java.util.UUID

@RestController
@RequestMapping("/api/v1/payment-order")

class PaymentOrderResource(private val paymentOrderService: PaymentOrderService) {

    @PostMapping("/create")
    fun getOrder(paymentOrderCreateRequestDTO: PaymentOrderCreateRequestDTO): ResponseEntity<CreatePaymentOrderResponseDTO> {
        val paymentOrder =  this.paymentOrderService.createPaymentOrder(paymentOrderCreateRequestDTO)
        return ResponseEntity.ok(paymentOrder)
    }

    @PostMapping("/process-payment")
    fun processPayment(processPaymentDTO: ProcessPaymentDTO): ResponseEntity<ResponseMessageDTO> {

        this.paymentOrderService.processPayment(processPaymentDTO)
        return ResponseEntity.ok(ResponseMessageDTO("Successfully processed"))
    }


    @GetMapping("/get-status/{paymentOrderId}")
    fun getStatus(paymentOrderId: UUID): ResponseEntity<PaymentOrderStatusResponseDTO> {
        val status = this.paymentOrderService.getPaymentOrderStatus(paymentOrderId)
        return ResponseEntity.ok(status)
    }
}
