package io.billie.invoices.resource

import io.billie.invoices.data.exceptions.NoOrderForInvoice
import io.billie.invoices.dto.*
import io.billie.invoices.dto.request.InvoiceRequest
import io.billie.invoices.dto.response.FullInvoiceResponse
import io.billie.invoices.service.InvoiceService
import io.billie.organisations.viewmodel.Entity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

/**
 * Endpoints for managing invoices.
 * Allow to prepare invoice for previously stored orders.
 */
@RestController
@RequestMapping("invoices")
class InvoiceResource(val service: InvoiceService) {

    @GetMapping
    @Operation(summary = "Show all invoices", description = "Returns list of invoices [complete view]")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All available invoices",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = FullInvoiceResponse::class)))
                    ))]
            )
        ]
    )
    fun index(): List<FullInvoiceResponse> = service.findInvoices()


    @PostMapping("prepare")
    @Operation(summary = "Prepare invoice based on order", description = "Returns invoice for specified order")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Invoice prepared successfully",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])
        ]
    )
    fun createInvoice(@Valid @RequestBody invoiceReq: InvoiceRequest): FullInvoiceResponse {
        try {
            return service.createInvoice(invoiceReq)
        } catch (e: NoOrderForInvoice) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Could not create invoice for request, reason: ${e.msg}"
            )
        }
    }
}