package io.billie.organisations.persistence

import io.billie.organisations.service.model.Organization
import java.util.UUID

interface OrganizationPersistenceAdapter {
    fun save(organization: Organization): UUID
    fun getAll(): List<Organization>
}
