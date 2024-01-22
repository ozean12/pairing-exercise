package io.billie.shipments.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Shipment not found")  // 404
class ShipmentNotFoundException(shipmentId: UUID) :
    RuntimeException("Shipment with ID $shipmentId not found")