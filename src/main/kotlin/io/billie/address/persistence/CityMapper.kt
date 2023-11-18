package io.billie.address.persistence

import io.billie.address.model.City
import io.billie.address.persistence.model.CityEntity
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface CityMapper {
    fun entityToModel(entity: CityEntity): City
}
