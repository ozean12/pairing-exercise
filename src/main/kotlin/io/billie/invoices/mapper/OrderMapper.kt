package io.billie.invoices.mapper

import io.billie.invoices.dto.CustomerOverview
import io.billie.invoices.dto.request.OrderRequest
import io.billie.invoices.dto.response.OrderResponse
import io.billie.invoices.model.Order
import java.time.LocalDateTime

fun OrderRequest.toModel(timestamp: LocalDateTime): Order = Order(
    orderId = this.orderId,
    orderTimestamp = timestamp,
    totalGross = this.totalGross,
    organisationId = this.organisationId,
    customerName = this.customer.name,
    customerAddress = this.customer.address,
    customerVATNumber = this.customer.vatNumber,
    products = this.products
)

fun Order.toResponse(): OrderResponse = OrderResponse(
    orderId = this.orderId,
    orderTimestamp = this.orderTimestamp,
    totalGross = this.totalGross,
    organisationId = this.organisationId,
    customer = CustomerOverview(this.customerName, this.customerVATNumber, this.customerAddress),
    products = this.products
)