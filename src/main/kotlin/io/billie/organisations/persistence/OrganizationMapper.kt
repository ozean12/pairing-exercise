package io.billie.organisations.persistence

import io.billie.organisations.persistence.model.ContactDetailsEntity
import io.billie.organisations.persistence.model.OrganizationEntity
import io.billie.organisations.service.model.ContactDetails
import io.billie.organisations.service.model.Organization
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = [ContactDetailsMapper::class])
interface OrganizationMapper {
    @Mapping(source = "organization.country.code", target = "countryCode")
    fun organizationModelToEntity(organization: Organization): OrganizationEntity
    @Mapping(source = "organizationEntity.countryCode", target = "country.code")
    fun organizationEntityToModel(organizationEntity: OrganizationEntity): Organization

}
