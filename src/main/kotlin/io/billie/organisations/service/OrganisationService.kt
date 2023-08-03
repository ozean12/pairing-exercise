package io.billie.organisations.service

import io.billie.organisations.data.InvalidAddress
import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.data.UnableToFindCountry
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        validateOrganisation(organisation)
        return db.create(organisation)
    }

    private fun validateOrganisation(organisation: OrganisationRequest) {
        if (!db.countryExists(organisation.countryCode))
            throw UnableToFindCountry(organisation.countryCode)

        val mainAddress = organisation.mainAddress
        if (mainAddress != null) {
            // sample validation
            if (!db.countryExists(mainAddress.countryCode))
                throw UnableToFindCountry(mainAddress.countryCode)
            if (mainAddress.zipCode.length > 10)
                throw InvalidAddress("zip_code", "ZIP code too long")
            if (mainAddress.houseNumber.isEmpty())
                throw InvalidAddress("house_number", "House number is mandatory")
        }
    }
}
