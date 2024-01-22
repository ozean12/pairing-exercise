package io.billie.shipments.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order not found")  // 404
class OrderNotFoundException(orderId: UUID) : RuntimeException("Order with ID $orderId not found")