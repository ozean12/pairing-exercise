package io.billie.organisations.service

import io.billie.countries.service.CountryService
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepository, val countryService: CountryService) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        val city = countryService.findCity(organisation.address.city, organisation.address.countryCode)
        return db.create(organisation, city.id)
    }

}
