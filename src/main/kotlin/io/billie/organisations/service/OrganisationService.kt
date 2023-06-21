package io.billie.organisations.service

import io.billie.countries.data.CityRepository
import io.billie.countries.data.CountryRepository
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCity
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.viewmodel.AddressDetailsRequest
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*

@Service
class OrganisationService(
    @Autowired private val db: OrganisationRepository,
    @Autowired private val countryRepository: CountryRepository,
    @Autowired private val cityRepository: CityRepository) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        validate(organisation)
        return db.create(organisation)
    }

    private fun validate(organisation: OrganisationRequest) {
        if(!countryRepository.isCountryCodeValid(organisation.countryCode)) {
            throw UnableToFindCountry(organisation.countryCode)
        }
        if (organisation.addressDetails != null) {
            if (organisation.countryCode != organisation.addressDetails.countryCode) {
                throw IllegalArgumentException("Country code of organisation and address mismatch")
            }
            validate(organisation.addressDetails)
        }
    }

    private fun validate(addressDetails: AddressDetailsRequest) {
        if (!countryRepository.isCountryCodeValid(addressDetails.countryCode)) {
            throw UnableToFindCountry(addressDetails.countryCode)
        }
        if (!cityRepository.isCityIdValid(addressDetails.cityId)) {
            throw UnableToFindCity(addressDetails.cityId)
        }
    }
}
