package io.billie.service

import io.billie.data.entity.Organisation
import java.util.Optional
import java.util.UUID

interface OrganisationService {

    fun getOrganisationById(organisationId: UUID) : Optional<Organisation>

    fun createOrganisation()
}