package io.billie.address.persistence

import io.billie.address.persistence.model.CountryEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CountryRepository: CrudRepository<CountryEntity, UUID>

