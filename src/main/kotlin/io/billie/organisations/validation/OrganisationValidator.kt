package io.billie.organisations.validation

import io.billie.countries.data.CountryRepository
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.dto.OrganisationRequest
import org.springframework.stereotype.Component

@Component
class OrganisationValidator (val countryRepository: CountryRepository) {

    fun validateOrganisationCreationRequest (organisationRequest: OrganisationRequest){
        if (countryRepository.findCountryByCode(organisationRequest.countryCode).isEmpty)
            throw UnableToFindCountry (organisationRequest.countryCode)
    }
}