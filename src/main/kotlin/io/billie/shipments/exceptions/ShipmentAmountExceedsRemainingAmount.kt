package io.billie.shipments.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.math.BigDecimal

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Shipment amount exceeds remaining amount")  // 400
class ShipmentAmountExceedsRemainingAmount(shipmentAmount: BigDecimal, remainingAmount: BigDecimal) :
    RuntimeException("Shipment amount $shipmentAmount exceeds remaining amount $remainingAmount of the order")