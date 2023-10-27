package io.billie.organisations.resource

import io.billie.organisations.data.CityWithIdDoesNotExist
import io.billie.organisations.data.OrganisationWithIdDoesNotExist
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.service.OrganisationService
import io.billie.organisations.viewmodel.Entity
import io.billie.organisations.viewmodel.OrganisationAddressRequest
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.Size


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

    @PostMapping(path = arrayOf("/{org_id}/addresses"))
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Accepted the address for organisation",
                content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Entity::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])]
    )
    fun postAddress(
        @Size(min = 36, max = 36)  @PathVariable("org_id") orgId: UUID,
        @Valid @RequestBody orgAddress: OrganisationAddressRequest
    ): Entity {
        try {
            val id = service.addAddressToOrg(orgId, orgAddress)
            return Entity(id)
        } catch (e: OrganisationWithIdDoesNotExist) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        } catch (e: CityWithIdDoesNotExist) {
            throw ResponseStatusException(BAD_REQUEST, e.message)
        }
    }
}
