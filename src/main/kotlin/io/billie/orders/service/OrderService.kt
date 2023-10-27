package io.billie.orders.service

import io.billie.orders.data.OrderRepository
import io.billie.orders.model.OrderRequest
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCountry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderService(val orderRepository: OrderRepository) {
    @Transactional
    fun createOrder(order: OrderRequest): UUID {
        //TODO validateOrder:
        // createdTime is in the past; organisation exists

        return orderRepository.create(order);
    }


}
