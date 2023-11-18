package io.billie.address.persistence

import io.billie.address.model.Country
import io.billie.address.persistence.model.CountryEntity
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface CountryMapper {
    fun entityToModel(model: CountryEntity?): Country?
}
