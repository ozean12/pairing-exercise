package io.billie.functional.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.data.repository.PaymentOrderRepository
import io.billie.dto.CardDetailsDTO
import io.billie.dto.PaymentOrderCreateRequestDTO
import io.billie.exception.NotFoundException
import io.billie.service.OrganisationService
import io.billie.service.PaymentOrderService
import io.billie.service.PaymentService
import io.billie.service.TransactionHistoryService
import io.billie.service.impl.PaymentOrderServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class)
class PaymentOrderServiceTest {


    @Mock
    private lateinit var paymentOrderRepository: PaymentOrderRepository

    @Mock
    private lateinit var organizationService: OrganisationService

    @Mock
    private lateinit var transactionHistoryService: TransactionHistoryService

    @Mock
    private lateinit var paymentService: PaymentService

    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var paymentOrderService: PaymentOrderService


    fun setUp() {
        this.paymentOrderService = PaymentOrderServiceImpl(
            paymentOrderRepository,
            organizationService,
            transactionHistoryService,
            objectMapper,
            paymentService
        )
    }

    private fun createPaymentOrderRequestMock(): PaymentOrderCreateRequestDTO {
        return PaymentOrderCreateRequestDTO(
            UUID.randomUUID(),
            10,
            50.79,
            "USD",
            CardDetailsDTO(
                "Some card",
                "Account Holder",
                236
            )
        )
    }

    @Test
    fun createPaymentOrderOrganisationNotFound() {

        val paymentOrderCreateRequest = this.createPaymentOrderRequestMock()

        Mockito.`when`(this.paymentOrderService.createPaymentOrder(paymentOrderCreateRequest))
            .thenThrow(NotFoundException("Organization is not found by provided ID: " + paymentOrderCreateRequest.organisationId))

        val exception: Exception = Assertions.assertThrows(
            NotFoundException::class.java
        ) {
            this.paymentOrderService.createPaymentOrder(paymentOrderCreateRequest)
        }

        val expectedMessage = "Organization is not found by provided ID: " + paymentOrderCreateRequest.organisationId
        val actualMessage = exception.message

        Assertions.assertTrue(actualMessage!!.contains(expectedMessage))
    }


}