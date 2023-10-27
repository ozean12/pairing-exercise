package io.billie.organisations.resource

import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.service.OrganisationService
import io.billie.organisations.viewmodel.*
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("organisations")
class OrganisationResource(val service: OrganisationService) {

    @GetMapping
    fun index(): List<OrganisationResponse> = service.findOrganisations()

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @RequestBody organisation: OrganisationRequest): Entity {
        try {
            val id = service.createOrganisation(organisation)
            return Entity(id)
        } catch (e: UnableToFindCountry) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

    @PostMapping(path = ["/address"])
    @ApiResponses(
            value = [
                ApiResponse(
                        responseCode = "200",
                        description = "Accepted the new address",
                        content = [
                            (Content(
                                    mediaType = "application/json",
                                    array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                            ))]
                ),
                ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )

    fun postAddress(@Valid @RequestBody address: AddressRequest): Entity {
        try {
            val id = service.addAddress(address)
            return Entity(id)
        } catch (e: UnableToFindCountry) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }

    @GetMapping("/{organisationCode}/addresses")
    fun addresses(@PathVariable("organisationCode") organisationCode: String): List<AddressResponse> {
        val addresses = service.findAdresses(organisationCode)
        if(addresses.isEmpty()) {
            throw ResponseStatusException(
                    NOT_FOUND,
                    "No addresses found for $organisationCode"
            )
        }
        return addresses
    }

}


