package io.billie.merchants.resource

import io.billie.invoicing.events.ShipmentEvent
import io.billie.customers.service.CustomerService
import io.billie.merchants.dto.MerchantRequest
import io.billie.merchants.dto.MerchantResponse
import io.billie.merchants.dto.ShipmentNotification
import io.billie.merchants.mapper.toMerchantResponse
import io.billie.merchants.service.MerchantService
import io.billie.merchants.service.ShipmentProcessor
import io.billie.merchants.validation.MerchantValidator
import io.billie.orders.service.OrderService
import io.billie.organisations.model.Entity
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger
import kotlin.streams.toList

@RestController
@RequestMapping ("/merchants")
class MerchantResource (
        val merchantService: MerchantService,
        val merchantValidator: MerchantValidator,
        val customerService: CustomerService,
        val orderService: OrderService,
        val shipmentProcessor: ShipmentProcessor) {

    val logger: Logger = Logger.getLogger(MerchantResource::javaClass.name)

    @GetMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "returns all merchants ",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = MerchantResponse::class)))
                            ))]
                )
            ]
    )
    fun getAllMerchants(): ResponseEntity<List<MerchantResponse>>{
        return ResponseEntity.ok(
                merchantService.findAllMerchants()
                        .stream()
                        .map { it.toMerchantResponse() }
                        .toList()
        )
    }

    // TODO: To be discussed in the meeting
    @PostMapping ("/{merchantUid}/notifyShipment")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "merchant notifies about the shipment of an order",
                        content = [Content()]
                ),
                ApiResponse(responseCode = "404", description = "Merchant/Customer/Order could not found", content = [Content()])]
    )
    fun notifyShipment (@PathVariable (name = "merchantUid") merchantUid: UUID,
                        @RequestBody shipmentNotification: ShipmentNotification)
    :ResponseEntity<Entity> {
        logger.info("shipment was notified by merchant: $merchantUid for: $shipmentNotification")

        val merchant = merchantService.findMerchantByUid(merchantUid)
        val customer = customerService.findCustomerByUid(shipmentNotification.customerUId)
        val order = orderService.findOrderByUid(shipmentNotification.orderUId)

        // TODO: more data enrichment can be done here
        val shipmentEvent = ShipmentEvent (merchant = merchant, customer = customer, order = order, shipmentTimestamp = LocalDateTime.now())

        val invoiceUid = shipmentProcessor.processShipmentEvent(shipmentEvent)

        logger.info("order was created for customer: ${customer.name} with uid: $invoiceUid")

        return ResponseEntity.ok(Entity(invoiceUid))
    }

    @GetMapping("/{merchantUid}")
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "return merchant information for given merchant uuid",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = MerchantResponse::class)))
                            ))]
                ),
                ApiResponse(responseCode = "404", description = "Merchant not found", content = [Content()])]
    )
    fun getMerchantInfo (@PathVariable (name = "merchantUid") merchantUid: String): ResponseEntity<MerchantResponse> {
       return ResponseEntity.ok(
               merchantService.findMerchantByUid(UUID.fromString(merchantUid))
                       .toMerchantResponse())
    }

    @PostMapping
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "201",
                        description = "Accepted the new merchant",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun registerMerchant (@RequestBody merchantRequest: MerchantRequest): ResponseEntity<Entity> {

        merchantValidator.validateMerchantCreationRequest(merchantRequest)

        val merchantUid: UUID= merchantService.createMerchant (merchantRequest)

        logger.info("merchant was created successfully with uid: $merchantUid")

        return ResponseEntity.status(HttpStatus.CREATED).body(Entity(merchantUid))
    }
}