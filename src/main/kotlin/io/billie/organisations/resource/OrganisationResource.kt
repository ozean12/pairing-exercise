package io.billie.organisations.resource

import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.data.UnableToFindOrganisation
import io.billie.organisations.service.OrganisationService
import io.billie.organisations.viewmodel.*
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("organisations")
class OrganisationResource(val service: OrganisationService) {

    @GetMapping
    fun index(): List<OrganisationResponse> = service.findOrganisations()

    @GetMapping("/{id}")
    fun getOrganisationById(@PathVariable id: UUID?): OrganisationResponse? {
        if (id == null) {
            throw ResponseStatusException(BAD_REQUEST, "Id must be specified")
        }

        return service.findOrganisationById(id)
    }

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

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @Valid @RequestBody organisation: OrganisationRequest): Entity {
        try {
            val id = service.updateOrganisation(id, organisation)
            return Entity(id)
        } catch (e: UnableToFindOrganisation) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }
}
