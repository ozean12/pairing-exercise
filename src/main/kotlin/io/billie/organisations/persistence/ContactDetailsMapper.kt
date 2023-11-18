package io.billie.organisations.persistence

import io.billie.organisations.persistence.model.ContactDetailsEntity
import io.billie.organisations.service.model.ContactDetails
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface ContactDetailsMapper {
    fun modelToEntity(contactDetails: ContactDetails): ContactDetailsEntity
    fun entityToModel(contactDetailsEntity: ContactDetailsEntity): ContactDetails
}
