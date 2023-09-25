package io.billie.invoicing.service

import io.billie.invoicing.events.ShipmentEvent
import io.billie.invoicing.data.InvoiceRepository
import io.billie.invoicing.dto.InstallmentRequest
import io.billie.invoicing.dto.InvoiceRequest
import io.billie.invoicing.exception.InvoiceNotFoundException
import io.billie.invoicing.model.Invoice
import io.billie.invoicing.model.InvoiceStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

@Service
class InvoiceServiceImpl (
        val invoiceRepository: InvoiceRepository,
        val installmentService: InstallmentService
        ): InvoiceService {

    override fun generateInvoiceForCustomer(shipmentEvent: ShipmentEvent): InvoiceRequest {
        val noOfInstallments = installmentService.getInstallmentsForCustomer(shipmentEvent.customer)

        val installmentAmount = shipmentEvent.order.amount.divide(BigDecimal.valueOf(noOfInstallments))

        val grandInvoice = InvoiceRequest (
                shipmentEvent.order.id,
                shipmentEvent.order.amount,
                InvoiceStatus.CREATED,
                shipmentEvent.customer.id,
                shipmentEvent.merchant.id,
                mutableListOf(),
                LocalDateTime.now(),
                LocalDateTime.now()
        )

        //TODO: due date shall be adapted according to installment strategy
        val installments = IntStream.rangeClosed(1, noOfInstallments.toInt())
                .mapToObj { it ->
                    InstallmentRequest (
                            shipmentEvent.order.id,
                            installmentAmount,
                            InvoiceStatus.CREATED,
                            shipmentEvent.customer.id,
                            shipmentEvent.merchant.id,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            LocalDateTime.now().plusMonths(it.toLong())
                    )
                }.toList()

        grandInvoice.installmentRequests.addAll(installments)

        return grandInvoice
    }

    override fun saveCustomerInvoiceRequest(invoice: InvoiceRequest): UUID {
        return invoiceRepository.createCustomerInvoices(invoice)
    }

    override fun findInvoices(): List<Invoice> {
        return invoiceRepository.findInvoices()
    }

    override fun findInvoiceByUid(invoiceUid: UUID): Invoice {
        return invoiceRepository.findCustomerInvoiceByUid(invoiceUid)
                .orElseThrow { InvoiceNotFoundException ("invoice with uid: $invoiceUid was not found!") }
    }
}