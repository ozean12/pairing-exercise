package io.billie.organisations.orders.service

import io.billie.organisations.orders.data.OrderNotFoundException
import io.billie.organisations.orders.data.OrderRepository
import io.billie.organisations.orders.data.ShipmentNotificationFailure
import io.billie.organisations.orders.domain.Order
import io.billie.organisations.orders.domain.OrderId
import io.billie.organisations.orders.domain.Shipment
import io.billie.organisations.orders.domain.ShipmentId
import io.billie.organisations.viewmodel.Entity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Offers services to fetch or create order per organisation
 */
@Service
class OrderService(val repository: OrderRepository) {

    @Transactional(readOnly = true)
    fun findById(orderId: OrderId): Order {
        return repository.findById(orderId)?: throw OrderNotFoundException(orderId)
    }

    @Transactional(readOnly = true)
    fun findByOrganisations(organisationId: Entity): List<Order> {
        return repository.findByOrganisation(organisationId)
    }

    @Transactional
    fun createOrder(order: Order): OrderId {
        return repository.create(order)
    }

    @Transactional
    fun notifyShipment(shipment: Shipment): ShipmentId {
        val order = findById(shipment.orderId)
        order.addShipment(shipment)
        return repository.update(order)
                ?: throw ShipmentNotificationFailure(shipment.shipmentAmount, shipment.orderId)
    }
}