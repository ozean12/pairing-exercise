package io.billie.merchants.service

import io.billie.invoicing.events.ShipmentEvent
import io.billie.invoicing.service.InvoiceService
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * TODO: better to send these events to a Kafka Topic to be processed by respected services
 * TODO: we need to perform the deduplication logic before processing
 */
@Service
class ShipmentProcessor (val invoiceService: InvoiceService) {

    fun processShipmentEvent (shipmentEvent: ShipmentEvent): UUID {
        // For the simplicity we call the invoicing service directly
        // TODO: call invoicing service to create invoice for the customer

        val invoice = invoiceService.generateInvoiceForCustomer(shipmentEvent)

        return invoiceService.saveCustomerInvoiceRequest(invoice)
    }
}