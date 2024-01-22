package io.billie.unit.shipments.service

import io.billie.orders.data.OrderRepository
import io.billie.orders.model.Order
import io.billie.orders.model.OrderStatus
import io.billie.shared.PostgresqlSharedContainer
import io.billie.shipments.data.ShipmentRepository
import io.billie.shipments.exceptions.OrderNotFoundException
import io.billie.shipments.exceptions.ShipmentAmountExceedsRemainingAmount
import io.billie.shipments.exceptions.ShipmentNotFoundException
import io.billie.shipments.model.Shipment
import io.billie.shipments.service.ShipmentService
import io.billie.shipments.viewmodel.ShipmentRequest
import io.billie.shipments.viewmodel.ShipmentResponse
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class ShipmentServiceTest {

    @MockK
    private lateinit var shipmentRepository: ShipmentRepository

    @MockK
    private lateinit var orderRepository: OrderRepository

    @InjectMockKs
    private lateinit var shipmentService: ShipmentService

    private lateinit var order: Order
    private lateinit var shipment: Shipment
    private lateinit var shipmentRequest: ShipmentRequest
    private lateinit var shipmentResponse: ShipmentResponse
    private val orderId = UUID.randomUUID()
    private val shipmentId = UUID.randomUUID()
    private val organizationId = UUID.randomUUID()
    private val shippedAt = LocalDateTime.now()

    @BeforeEach
    fun setUp() {

        order = Order(
            orderId,
            organizationId,
            BigDecimal("1000.00"),
            OrderStatus.PENDING,
            10,
            LocalDateTime.now()
        )
        shipmentRequest = ShipmentRequest(orderId, BigDecimal("100.00"), shippedAt)
        shipment = Shipment(shipmentId, orderId, BigDecimal("100.00"), shippedAt)
        shipmentResponse = ShipmentResponse(shipmentId, orderId, BigDecimal("100.00"), LocalDateTime.now())


        every { orderRepository.findById(orderId) } returns order
        every { shipmentRepository.createShipment(any()) } returns (shipmentId)
        every { shipmentRepository.findTotalShippedAmountByOrder(orderId) } returns BigDecimal.ZERO

    }

    @Test
    fun createShipment_success() {

        val result = shipmentService.createShipment(shipmentRequest)

        verify { orderRepository.findById(orderId) }
        verify { shipmentRepository.createShipment(any()) }
        verify { shipmentRepository.findTotalShippedAmountByOrder(orderId) }

        assertEquals(shipmentRequest.shipmentAmount, result.shipmentAmount)
        assertEquals(shipmentResponse.id, result.id)
        assertEquals(shipmentResponse.orderId, result.orderId)

    }


    @Test
    fun createShipment_updateOrderStatus() {


        every { shipmentRepository.findTotalShippedAmountByOrder(orderId) } returns
                BigDecimal.ZERO andThen BigDecimal("1000.00")

        justRun { orderRepository.updateStatus(OrderStatus.SHIPPED, orderId) }

        val result = shipmentService.createShipment(shipmentRequest)

        verify { orderRepository.findById(orderId) }
        verify { shipmentRepository.createShipment(any()) }
        verify { shipmentRepository.findTotalShippedAmountByOrder(orderId) }
        verify { orderRepository.updateStatus(OrderStatus.SHIPPED, orderId) }

        assertEquals(shipmentRequest.shipmentAmount, result.shipmentAmount)
        assertEquals(shipmentResponse.id, result.id)
        assertEquals(shipmentResponse.orderId, result.orderId)

    }

    @Test
    fun createShipment_orderNotFound() {
        every { orderRepository.findById(orderId) } returns null

        assertThrows<OrderNotFoundException> {
            shipmentService.createShipment(shipmentRequest)
        }
    }

    @Test
    fun createShipment_amountExceeds() {

        every { shipmentRepository.findTotalShippedAmountByOrder(orderId) } returns BigDecimal("950.00")

        assertThrows<ShipmentAmountExceedsRemainingAmount> {
            shipmentService.createShipment(shipmentRequest)
        }
    }

    @Test
    fun createMultipleShipmentsForSameOrder_success() {

        val shipmentAmount1 = BigDecimal("300.00")
        val shipmentAmount2 = BigDecimal("700.00")
        val totalShipmentAmount = shipmentAmount1.add(shipmentAmount2)

        justRun { orderRepository.updateStatus(OrderStatus.SHIPPED, orderId) }
        every { shipmentRepository.findTotalShippedAmountByOrder(orderId) } returnsMany listOf(
            BigDecimal.ZERO,
            shipmentAmount1, shipmentAmount1, totalShipmentAmount
        )

        shipmentService.createShipment(shipmentRequest.copy(shipmentAmount = shipmentAmount1))
        shipmentService.createShipment(shipmentRequest.copy(shipmentAmount = shipmentAmount2))

        verify(exactly = 2) { shipmentRepository.createShipment(any()) }
        verify(exactly = 1) { orderRepository.updateStatus(OrderStatus.SHIPPED, orderId) }
    }

    @Test
    fun createPartialShipmentForOrder_statusUnchanged() {

        val partialShipmentAmount = BigDecimal("500.00")
        every { shipmentRepository.findTotalShippedAmountByOrder(orderId) } returns BigDecimal.ZERO

        shipmentService.createShipment(shipmentRequest.copy(shipmentAmount = partialShipmentAmount))

        verify { shipmentRepository.createShipment(any()) }
        verify(exactly = 0) { orderRepository.updateStatus(OrderStatus.SHIPPED, orderId) }
    }

    @Test
    fun triggerPaymentProcessOnShipmentCreation() {

        val shipmentServiceSpyMock =
            spyk<ShipmentService>(ShipmentService(shipmentRepository, orderRepository), recordPrivateCalls = true)

        val shipmentAmount = BigDecimal("1000.00")
        justRun {
            shipmentServiceSpyMock invoke "triggerPaymentToMerchant" withArguments listOf(
                order,
                shipment.copy(shipmentAmount = shipmentAmount)
            )
        }

        shipmentServiceSpyMock.createShipment(shipmentRequest.copy(shipmentAmount = shipmentAmount))

        verify {
            shipmentServiceSpyMock invoke "triggerPaymentToMerchant" withArguments listOf(
                order,
                shipment.copy(shipmentAmount = shipmentAmount)
            )
        }
    }

    @Test
    fun getShipmentsByOrderId_success() {

        every { shipmentRepository.findByOrderId(orderId) } returns listOf(shipment)


        val results = shipmentService.getShipmentsByOrderId(orderId)

        assertEquals(1, results.size)
        assertEquals(shipmentResponse.shipmentAmount, results.first().shipmentAmount)
        assertEquals(shipmentResponse.orderId, results.first().orderId)
        assertEquals(shipmentResponse.id, results.first().id)
    }

    @Test
    fun getShipmentById_success() {
        every { shipmentRepository.findById(shipmentId) } returns shipment

        val result = shipmentService.getShipmentById(shipmentId)

        assertEquals(shipmentResponse.shipmentAmount, result.shipmentAmount)
        assertEquals(shipmentResponse.orderId, result.orderId)
        assertEquals(shipmentResponse.id, result.id)

    }

    @Test
    fun getShipmentById_notFound_throwsException() {
        every { shipmentRepository.findById(shipmentId) } returns null

        assertThrows<ShipmentNotFoundException> {
            shipmentService.getShipmentById(shipmentId)
        }
    }

}