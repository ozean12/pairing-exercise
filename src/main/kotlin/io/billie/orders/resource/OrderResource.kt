package io.billie.orders.resource

import io.billie.orders.data.OrderAlreadyExists
import io.billie.orders.model.OrderRequest
import io.billie.orders.service.OrderService
import io.billie.shared.model.Entity
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("orders")
class OrderResource(val service: OrderService) {

    @PostMapping
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
                ApiResponse(responseCode = "409", description = "Conflict", content = [Content()])]
    )
    fun post(@Valid @RequestBody order: OrderRequest): Entity {
        try {
            val id = service.createOrder(order)
            return Entity(id)
        } catch (e: OrderAlreadyExists) {
            throw ResponseStatusException(CONFLICT, e.message)
        }
    }

    @PatchMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "The order was updated",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun patch(@Valid @RequestBody order: OrderRequest) { // TODO add return type
        try {
            // TODO use for notifying the order was shipped by updating the state
        } catch (e: OrderAlreadyExists) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }
}
