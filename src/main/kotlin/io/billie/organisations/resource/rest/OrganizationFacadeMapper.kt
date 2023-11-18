package io.billie.organisations.resource.rest

import io.billie.organisations.resource.rest.model.OrganizationCreationRequest
import io.billie.organisations.resource.rest.model.OrganizationResponse
import io.billie.organisations.service.model.Organization
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface OrganizationFacadeMapper {
    @Mapping(source = "request.countryCode", target = "country.code")
    fun bodyToModel(request: OrganizationCreationRequest): Organization
    fun modelToBody(model: Organization): OrganizationResponse

}
