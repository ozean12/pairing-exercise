package io.billie.service.impl

import io.billie.data.entity.Organisation
import io.billie.data.repository.OrganisationsRepository
import io.billie.service.OrganisationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganisationServiceImpl(
    private val organisationsRepository: OrganisationsRepository
)
    : OrganisationService {
    override fun getOrganisationById(organisationId: UUID): Optional<Organisation> {
       return this.organisationsRepository.findById(organisationId)
    }

    override fun createOrganisation() {
        TODO("Not yet implemented")
    }
}
