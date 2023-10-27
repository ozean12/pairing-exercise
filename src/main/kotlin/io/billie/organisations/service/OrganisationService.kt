package io.billie.organisations.service

import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.data.UnableToFindOrganisation
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()
    fun findOrganisationById(id: UUID): OrganisationResponse? = db.findOrganisationById(id)

    @Throws(UnableToFindCountry::class)
    fun createOrganisation(organisation: OrganisationRequest): UUID {
        return db.create(organisation)
    }

    @Throws(UnableToFindOrganisation::class)
    fun updateOrganisation(id: UUID, organisation: OrganisationRequest): UUID {
        return db.update(id, organisation)
    }

}
