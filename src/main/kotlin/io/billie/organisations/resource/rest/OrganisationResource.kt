package io.billie.organisations.resource.rest

import io.billie.organisations.persistence.UnableToFindCountry
import io.billie.organisations.model.*
import io.billie.organisations.resource.rest.model.CreationResponse
import io.billie.organisations.resource.rest.model.OrganizationCreationRequest
import io.billie.organisations.resource.rest.model.OrganizationResponse
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
class OrganisationResource(val facade: OrganizationFacade) {

    @GetMapping
    fun index(): List<OrganizationResponse> = facade.getAll()

    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the new organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = CreationResponse::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun post(@Valid @RequestBody organisation: OrganizationCreationRequest): CreationResponse {
        return facade.add(organisation)
    }

}
