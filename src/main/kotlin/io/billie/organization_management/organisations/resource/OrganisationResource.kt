package io.billie.organization_management.organisations.resource

import io.billie.organization_management.organisations.data.CountryNotFoundException
import io.billie.organization_management.organisations.service.OrganisationService
import io.billie.organization_management.organisations.model.Entity
import io.billie.organization_management.organisations.model.OrganisationRequest
import io.billie.organization_management.organisations.model.OrganisationResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("organisations")
class OrganisationResource(val service: OrganisationService) {

    @GetMapping
    fun organisations(): List<OrganisationResponse> = service.findOrganisations()

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new organisation",
                content = [
                    (
                        Content(
                            mediaType = "application/json",
                            array = (ArraySchema(schema = Schema(implementation = Entity::class))),
                        )
                        ),
                ],
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
        ],
    )
    fun addOrganisation(
        @Valid
        @RequestBody
        organisation: OrganisationRequest,
    ): Entity {
        return try {
            Entity(service.createOrganisation(organisation))
        } catch (e: CountryNotFoundException) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }
}
