package io.billie.functional.data

import java.util.UUID

object InvoiceFixtures {

    fun notValidInvoiceRequest() = """ { "strange_id": "doesnt_matter" } """

    fun validInvoiceRequestWithExtra() = """ { "order_id": "doesnt_matter", "other_field": 1 } """

    fun validInvoiceRequest() = validInvoiceRequest(UUID.randomUUID())

    fun validInvoiceRequest(orderId: UUID) = """ { "order_id": "$orderId" } """

}