package io.billie.organisations.service

import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationResponse> =
        db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID =
        db.create(organisation)

}
