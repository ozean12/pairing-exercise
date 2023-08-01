package io.billie.organisations.resource

import io.billie.getHttpException
import io.billie.organisations.service.OrganisationService
import io.billie.organisations.viewmodel.*
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
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
        } catch (e: Exception) {
            throw getHttpException(e)
        }
    }


    @PostMapping("/{organisationId}/addresses")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Added address to organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(
        @Valid @RequestBody addressRequest: AddressRequest,
        @PathVariable organisationId: String,
    ): Entity {
        try {
            val id = service.addAddressToOrganisation(organisationId, addressRequest)
            return Entity(id)
        } catch (e: Exception) {
            throw getHttpException(e)
        }
    }

}
