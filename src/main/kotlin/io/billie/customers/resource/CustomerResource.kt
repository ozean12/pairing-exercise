package io.billie.customers.resource

import io.billie.customers.dto.CustomerRequest
import io.billie.customers.dto.CustomerResponse
import io.billie.customers.mapper.toCustomerResponse
import io.billie.customers.service.CustomerService
import io.billie.orders.dto.OrderResponse
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
@RequestMapping ("/customers")
class CustomerResource (val customerService: CustomerService) {
    @GetMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "returns all customers ",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = CustomerResponse::class)))
                            ))]
                )
            ]
    )
    fun findCustomers (): ResponseEntity<List<CustomerResponse>>{
        return ResponseEntity.ok(customerService.findCustomers()
                .stream().map { it.toCustomerResponse() }
                .toList())
    }

    @PostMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "201",
                        description = "Accepted the new customer",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun createCustomer(@RequestBody customerRequest: CustomerRequest): ResponseEntity<Entity>{
        val customerId = customerService.createCustomer(customerRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(Entity(customerId))
    }
}