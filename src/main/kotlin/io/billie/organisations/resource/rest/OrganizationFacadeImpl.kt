package io.billie.organisations.resource.rest

import io.billie.organisations.resource.rest.model.CreationResponse
import io.billie.organisations.resource.rest.model.OrganizationCreationRequest
import io.billie.organisations.resource.rest.model.OrganizationResponse
import io.billie.organisations.service.OrganizationService
import org.springframework.stereotype.Component

@Component
class OrganizationFacadeImpl(private val organizationService: OrganizationService,
                             private val facadeMapper: OrganizationFacadeMapper): OrganizationFacade{
    override fun getAll(): List<OrganizationResponse> {
        return organizationService.getAll().map { facadeMapper.modelToBody(it) }
    }

    override fun add(request: OrganizationCreationRequest): CreationResponse {
        val organizationModel = facadeMapper.bodyToModel(request);
        val id = organizationService.add(organizationModel)
        return CreationResponse(id)
    }
}
