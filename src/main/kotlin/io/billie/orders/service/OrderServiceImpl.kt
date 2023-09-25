package io.billie.orders.service

import io.billie.orders.data.OrderRepository
import io.billie.orders.dto.OrderRequest
import io.billie.orders.execption.OrderNotFoundException
import io.billie.orders.model.Order
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderServiceImpl (val orderRepository: OrderRepository) : OrderService {
    override fun findOrderByUid(orderUid: UUID): Order {
        return orderRepository.findOrderByUid(orderUid)
                .orElseThrow { OrderNotFoundException ("order with Uid: $orderUid was not found!") }
    }

    override fun findOrders(): List<Order> {
        return orderRepository.findOrders()
    }

    override fun createOrder(orderRequest: OrderRequest): UUID {
        return orderRepository.createOrder(orderRequest)
    }
}