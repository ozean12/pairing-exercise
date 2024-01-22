package io.billie.shipments.service

import io.billie.orders.data.OrderRepository
import io.billie.orders.model.Order
import io.billie.orders.model.OrderStatus
import io.billie.shipments.data.ShipmentRepository
import io.billie.shipments.exceptions.OrderNotFoundException
import io.billie.shipments.exceptions.ShipmentAmountExceedsRemainingAmount
import io.billie.shipments.exceptions.ShipmentNotFoundException
import io.billie.shipments.model.Shipment
import io.billie.shipments.viewmodel.ShipmentRequest
import io.billie.shipments.viewmodel.ShipmentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class ShipmentService(
    private val shipmentRepository: ShipmentRepository,
    private val orderRepository: OrderRepository
) {

    @Transactional
    fun createShipment(shipmentRequest: ShipmentRequest): ShipmentResponse {

        val order =
            orderRepository.findById(shipmentRequest.orderId) ?: throw OrderNotFoundException(shipmentRequest.orderId)

        validateShipmentAmount(order, shipmentRequest.shipmentAmount)

        val shipment = buildShipment(shipmentRequest)

        val createdShipment = shipmentRepository.createShipment(shipment).let {
            shipment.copy(id = it)
        }

        val updatedOrder = updateOrderStatus(order)

        triggerPaymentToMerchant(updatedOrder, createdShipment)

        return createdShipment.toShipmentResponse()
    }


    @Transactional(readOnly = true)
    fun getShipmentsByOrderId(orderId: UUID): List<ShipmentResponse> {
        return shipmentRepository.findByOrderId(orderId).map { it.toShipmentResponse() }
    }

    @Transactional(readOnly = true)
    fun getShipmentById(shipmentId: UUID): ShipmentResponse {
        val shipment = shipmentRepository.findById(shipmentId)
            ?: throw ShipmentNotFoundException(shipmentId)

        return shipment.toShipmentResponse()
    }

    private fun validateShipmentAmount(order: Order, shipmentAmount: BigDecimal) {
        val totalShippedAmount = shipmentRepository.findTotalShippedAmountByOrder(order.id)
        val remainingAmount = order.totalAmount.subtract(totalShippedAmount)
        if (shipmentAmount > remainingAmount) {
            throw ShipmentAmountExceedsRemainingAmount(shipmentAmount, remainingAmount)
        }
    }

    private fun updateOrderStatus(order: Order): Order {

        val totalShippedAmount = shipmentRepository.findTotalShippedAmountByOrder(order.id)
        if (totalShippedAmount >= order.totalAmount) {
            orderRepository.updateStatus(OrderStatus.SHIPPED, orderId = order.id)
            return order.copy(status = OrderStatus.SHIPPED)
        }
        return order
    }

    private fun triggerPaymentToMerchant(order: Order, shipment: Shipment) {
        // TODO Logic to trigger the payment process
    }

    private fun Shipment.toShipmentResponse(): ShipmentResponse {
        return ShipmentResponse(
            id = this.id!!,
            orderId = this.orderId,
            shipmentAmount = this.shipmentAmount,
            shippedAt = this.shippedAt
        )
    }

    private fun buildShipment(shipmentRequest: ShipmentRequest): Shipment {
        val shipment = Shipment(
            orderId = shipmentRequest.orderId,
            shipmentAmount = shipmentRequest.shipmentAmount,
            shippedAt = shipmentRequest.shippedAt
        )
        return shipment
    }

}