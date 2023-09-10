package io.billie.invoices.resource

import io.billie.invoices.data.exceptions.DuplicatedRecord
import io.billie.invoices.data.exceptions.NoOrganisationForOrder
import io.billie.invoices.dto.request.OrderRequest
import io.billie.invoices.dto.response.OrderResponse
import io.billie.invoices.dto.response.SimpleSuccessResponse
import io.billie.invoices.resource.OrderResource.OrderResource.successOrderCreationMsg
import io.billie.invoices.service.OrderService
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
import java.util.UUID
import javax.validation.Valid

/**
 * Endpoints for managing orders.
 * All info in order is required.
 */
@RestController
@RequestMapping("orders")
class OrderResource(val service: OrderService) {

    @GetMapping
    @Operation(summary = "Show all orders", description = "Returns list of orders")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All available orders",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = OrderResponse::class)))
                    ))]
            )
        ]
    )
    fun index(): List<OrderResponse> = service.findOrders()


    @PostMapping
    @Operation(summary = "New order to process", description = "Returns 200 on successful saving")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Save new order to process",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = SimpleSuccessResponse::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(
                responseCode = "409",
                description = "Conflict, order already exists or cannot be set saved due other reasons ",
                content = [Content()]
            )
        ]
    )
    fun newOrderToProcess(@Valid @RequestBody order: OrderRequest): SimpleSuccessResponse {
        try {
            service.saveOrder(order)
            return SimpleSuccessResponse(successOrderCreationMsg(order.orderId))
        } catch (e: DuplicatedRecord) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Could not process order because the record with same id ${e.id} already exists"
            )
        } catch (e: NoOrganisationForOrder) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.msg)
        }
    }

    object OrderResource {
        fun successOrderCreationMsg(orderId: UUID) = "Order with id $orderId has been saved successfully"
    }
}