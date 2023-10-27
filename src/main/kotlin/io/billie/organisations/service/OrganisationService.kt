package io.billie.organisations.service

import io.billie.organisations.data.OrganisationRepository
import io.billie.organisations.viewmodel.AddressRequest
import io.billie.organisations.viewmodel.AddressResponse
import io.billie.organisations.viewmodel.OrganisationRequest
import io.billie.organisations.viewmodel.OrganisationResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationService(val db: OrganisationRepository) {

    fun findOrganisations(): List<OrganisationResponse> = db.findOrganisations()

    fun createOrganisation(organisation: OrganisationRequest): UUID {
        return db.create(organisation)
    }

    fun addAddress(address: AddressRequest): UUID{
        return db.addAddress(address)
    }

    fun findAdresses(orgCode: String): List<AddressResponse> = db.findAdressesByOrgCode(orgCode)

}
