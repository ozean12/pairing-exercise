package io.billie.orders.resource

import io.billie.customers.service.CustomerService
import io.billie.orders.dto.OrderRequest
import io.billie.orders.dto.OrderResponse
import io.billie.orders.mapper.toOrderResponse
import io.billie.orders.service.OrderService
import io.billie.orders.validation.OrderRequestValidator
import io.billie.organisations.model.Entity
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.streams.toList

@RestController
@RequestMapping ("/orders")
class OrderResource (val orderService: OrderService,
        val customerService: CustomerService,
        val orderRequestValidator: OrderRequestValidator) {

    @GetMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "returns all orders ",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = OrderResponse::class)))
                            ))]
                )
            ]
    )
    fun findOrders (): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(
                orderService.findOrders().stream()
                        .map { it.toOrderResponse() }
                        .toList()
        )
    }

    @PostMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "201",
                        description = "Accepted the new order",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun createOrder (@RequestBody orderRequest: OrderRequest): ResponseEntity<Entity> {

        // TODO: it's used for validating the customerId but will be used later
        val customer = customerService.findCustomerByUid(orderRequest.customerId)

        orderRequestValidator.validateOrderRequest(orderRequest)

        val orderUid = orderService.createOrder(orderRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(Entity(orderUid))
    }
}