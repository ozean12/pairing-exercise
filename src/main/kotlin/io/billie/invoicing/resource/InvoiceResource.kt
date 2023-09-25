package io.billie.invoicing.resource

import io.billie.invoicing.dto.InvoiceResponse
import io.billie.invoicing.mapper.toInvoiceResponse
import io.billie.invoicing.service.InvoiceService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.streams.toList

@RestController
@RequestMapping ("/invoices")
class InvoiceResource  (val invoiceService: InvoiceService) {

    @GetMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "returns all invoices ",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = InvoiceResponse::class)))
                            ))]
                )
            ]
    )
    fun findInvoices (): ResponseEntity<List<InvoiceResponse>> {
        val invoices = invoiceService.findInvoices()
                .stream()
                .map { it.toInvoiceResponse() }
                .toList()

        return ResponseEntity.ok(invoices)
    }
}