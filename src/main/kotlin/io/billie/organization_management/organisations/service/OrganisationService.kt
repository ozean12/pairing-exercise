package io.billie.organization_management.organisations.service

import io.billie.organization_management.countries.data.Country
import io.billie.organization_management.countries.data.CountryRepository
import io.billie.organization_management.countries.model.CountryResponse
import io.billie.organization_management.organisations.data.*
import io.billie.organization_management.organisations.model.ContactDetailRequest
import io.billie.organization_management.organisations.model.ContactDetailResponse
import io.billie.organization_management.organisations.model.OrganisationRequest
import io.billie.organization_management.organisations.model.OrganisationResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrganisationService(
    val organisationRepository: OrganisationRepository,
    val countryRepository: CountryRepository,
) {

    @Transactional(readOnly = true)
    fun findOrganisations(): List<OrganisationResponse> {
        return organisationRepository.findAll().map {
            OrganisationResponse(
                id = it.id!!,
                name = it.name,
                dateFounded = it.dateFounded,
                vatNumber = it.vatNumber,
                registrationNumber = it.registrationNumber,
                legalEntityType = it.legalEntityType,
                country = mapToCountryResponse(it.country),
                contactDetail = mapToContactDetailResponse(it.contactDetail),
            )
        }
    }

    private fun mapToCountryResponse(country: Country): CountryResponse {
        return country.let {
            CountryResponse(
                id = it.id!!,
                name = it.name,
                countryCode = it.code,
            )
        }
    }

    private fun mapToContactDetailResponse(contactDetail: ContactDetail): ContactDetailResponse {
        return contactDetail.let {
            ContactDetailResponse(
                id = it.id,
                phoneNumber = it.phoneNumber,
                fax = it.fax,
                email = it.email,
            )
        }
    }

    @Transactional
    fun createOrganisation(organisationRequest: OrganisationRequest): UUID {
        val country = countryRepository.findByCode(organisationRequest.countryCode) ?: throw CountryNotFoundException(organisationRequest.countryCode)

        val organisation = Organisation(
            id = null,
            name = organisationRequest.name,
            dateFounded = organisationRequest.dateFounded,
            country = country,
            vatNumber = organisationRequest.vatNumber,
            registrationNumber = organisationRequest.registrationNumber,
            legalEntityType = organisationRequest.legalEntityType,
            contactDetail = mapToContactDetail(organisationRequest.contactDetail),
        )

        return organisationRepository.save(organisation).id!!
    }

    private fun mapToContactDetail(contactDetailRequest: ContactDetailRequest): ContactDetail {
        return contactDetailRequest.let {
            ContactDetail(
                id = null,
                phoneNumber = it.phoneNumber,
                fax = it.fax,
                email = it.email,
            )
        }
    }
}
