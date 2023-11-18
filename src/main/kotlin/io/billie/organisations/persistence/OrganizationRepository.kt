package io.billie.organisations.persistence

import io.billie.organisations.persistence.model.OrganizationEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface OrganizationRepository : CrudRepository<OrganizationEntity, UUID>{
}
