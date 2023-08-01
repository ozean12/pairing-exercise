package io.billie.organisations.service

import io.billie.countries.data.CityRepository
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindOrganisation
import io.billie.organisations.viewmodel.AddressRequest
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(
    private val organisationRepository: OrganisationRepository,
    private val cityRepository: CityRepository,
) {

    fun findOrganisations(): List<OrganisationResponse> = organisationRepository.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        return organisationRepository.create(organisation)
    }

    fun addAddressToOrganisation(organisationId: String, addressRequest: AddressRequest): UUID {
        validateOrganisationExists(organisationId)
        validateCityExists(addressRequest.cityId)
        val addressId = organisationRepository.createAddress(addressRequest)
        organisationRepository.addAddressToOrganisation(UUID.fromString(organisationId), addressId)
        return addressId
    }

    private fun validateCityExists(cityId: String) {
        if (!cityRepository.existsById(UUID.fromString(cityId))) {
            throw UnableToFindCity(cityId)
        }
    }

    private fun validateOrganisationExists(organisationId: String) {
        if (!organisationRepository.existsById(UUID.fromString(organisationId))) {
            throw UnableToFindOrganisation(organisationId)
        }
    }
}
