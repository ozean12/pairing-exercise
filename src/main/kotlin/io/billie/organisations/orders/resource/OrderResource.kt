package io.billie.organisations.orders.resource

import io.billie.organisations.orders.data.OrderNotFoundException
import io.billie.organisations.orders.data.OrganisationNotFoundException
import io.billie.organisations.orders.data.ShipmentAmountExceedOrderTotalException
import io.billie.organisations.orders.domain.*
import io.billie.organisations.orders.service.OrderService
import io.billie.organisations.orders.view.OrderDto
import io.billie.organisations.orders.view.OrderRequest
import io.billie.organisations.orders.view.ShipmentRequest
import io.billie.organisations.viewmodel.Entity
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.stream.Collectors
import javax.validation.Valid

/**
 * Rest end-points to find orders by organisation or create order for an organisation
 */
@RestController
@RequestMapping("v1")
class OrderResource(val orderService: OrderService) {

    @GetMapping("/organisations/{organisationId}/orders")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Returns list of orders of an organisation",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = OrderDto::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun getByOrganisayion(@Valid @PathVariable organisationId: UUID): List<OrderDto> {
        try {
            val orders = orderService.findByOrganisations(Entity(organisationId))
            return mapToDtos(orders)
        } catch (e: OrganisationNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/organisations/{organisationId}/orders")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Accepted the new order",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @PathVariable organisationId: UUID,
             @Valid @RequestBody orderRequest: OrderRequest): OrderId {
        try {
            val orderId = orderService.createOrder(mapToDomain(organisationId, orderRequest))
            return orderId
        } catch (e: OrganisationNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/orders/{orderId}")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Returns order matching the id",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    schema = Schema(implementation = OrderDto::class)
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun getById(@Valid @PathVariable orderId: UUID): OrderDto {
        try {
            val order = orderService.findById(OrderId(orderId))
            return mapToDto(order)
        } catch (e: OrderNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PostMapping("/orders/{orderId}/shipments")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Registered shipment dispatch for an order",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @PathVariable orderId: UUID,
             @Valid @RequestBody shipmentRequest: ShipmentRequest): ShipmentId {
        try {
            return orderService.notifyShipment(mapToDomain(orderId, shipmentRequest))
        } catch (e: OrderNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: ShipmentAmountExceedOrderTotalException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    private fun mapToDtos(orders: List<Order>): List<OrderDto> {
        return orders.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList())
    }

    private fun mapToDto(order: Order): OrderDto {
        return OrderDto(order.orderId!!.id, order.organisationId.id, order.orderAmount.amount)
    }

    private fun mapToDomain(organisationId: UUID, orderRequest: OrderRequest): Order {
        return Order(null, Entity(organisationId), OrderAmount(orderRequest.amount), mutableSetOf())
    }

    private fun mapToDomain(orderId: UUID, shipmentRequest: ShipmentRequest): Shipment {
        return Shipment(null, OrderId(orderId), ShipmentAmount(shipmentRequest.amount))
    }
}