package io.billie.invoices.service

import io.billie.invoices.data.OrderRepository
import io.billie.invoices.dto.request.OrderRequest
import io.billie.invoices.mapper.toModel
import io.billie.invoices.mapper.toResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * Service for managing orders.
 * Provides number of functions for storing and getting order from repo.
 */
@Service
class OrderService(val db: OrderRepository) {

    fun findOrders() = db.findOrders().map { it.toResponse() }

    fun saveOrder(orderReq: OrderRequest) {
        val order = orderReq.toModel(LocalDateTime.now())
        return db.create(order)
    }
}