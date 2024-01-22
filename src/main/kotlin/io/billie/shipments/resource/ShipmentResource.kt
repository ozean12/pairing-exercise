package io.billie.shipments.resource

import io.billie.organisations.viewmodel.Entity
import io.billie.shipments.service.ShipmentService
import io.billie.shipments.viewmodel.ShipmentRequest
import io.billie.shipments.viewmodel.ShipmentResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("shipments")
class ShipmentResource(private val shipmentService: ShipmentService) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Accepted the new shipment",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ShipmentResponse::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Order Not found", content = [Content()])],
    )
    @PostMapping
    fun createShipment(
        @Valid @RequestBody shipmentRequest: ShipmentRequest
    ): ResponseEntity<ShipmentResponse> {
        val shipmentResponse =
            shipmentService.createShipment(shipmentRequest)
        return ResponseEntity(shipmentResponse, HttpStatus.CREATED)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Get shipments by order id",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ShipmentResponse::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Order Not found", content = [Content()])],
    )
    @GetMapping("/order/{orderId}")
    fun getShipmentsByOrder(@PathVariable orderId: UUID): ResponseEntity<List<ShipmentResponse>> {
        val shipments = shipmentService.getShipmentsByOrderId(orderId)
        return ResponseEntity(shipments, HttpStatus.OK)
    }


    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Get shipment by shipment id",
                content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ShipmentResponse::class)
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Shipment Not found", content = [Content()])],
    )
    @GetMapping("/{shipmentId}")
    fun getShipmentById(@PathVariable shipmentId: UUID): ResponseEntity<ShipmentResponse> {
        val shipment = shipmentService.getShipmentById(shipmentId)
        return ResponseEntity(shipment, HttpStatus.OK)
    }
}