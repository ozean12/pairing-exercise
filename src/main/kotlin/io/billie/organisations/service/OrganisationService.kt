package io.billie.organisations.service

import io.billie.organisations.persistence.OrganisationRepo
import io.billie.organisations.resource.rest.model.OrganisationRequest
import io.billie.organisations.resource.rest.model.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepo) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        return db.create(organisation)
    }

}
