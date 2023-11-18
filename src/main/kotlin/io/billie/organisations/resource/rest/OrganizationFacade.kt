package io.billie.organisations.resource.rest

import io.billie.organisations.resource.rest.model.CreationResponse
import io.billie.organisations.resource.rest.model.OrganizationCreationRequest
import io.billie.organisations.resource.rest.model.OrganizationResponse

interface OrganizationFacade {
    fun getAll(): List<OrganizationResponse>
    fun add(request: OrganizationCreationRequest): CreationResponse

}
