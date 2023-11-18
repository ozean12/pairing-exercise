package io.billie.organisations.service

import io.billie.organisations.service.model.Organization
import java.util.UUID

interface OrganizationService {
    /**
     * IllegalArgumentException if countryCode does not exist
     */
    fun add(organization: Organization): UUID
    fun getAll(): List<Organization>
}
