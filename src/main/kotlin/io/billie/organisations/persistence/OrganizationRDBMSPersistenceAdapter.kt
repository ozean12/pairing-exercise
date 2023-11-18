package io.billie.organisations.persistence

import io.billie.address.persistence.CountryRepository
import io.billie.organisations.persistence.model.ContactDetailsEntity
import io.billie.organisations.persistence.model.OrganizationEntity
import io.billie.organisations.service.model.ContactDetails
import io.billie.organisations.service.model.Organization
import org.springframework.stereotype.Component
import java.util.*
import javax.transaction.Transactional

@Component
class OrganizationRDBMSPersistenceAdapter(
    private val organizationRepository: OrganizationRepository,
    private val mapper: OrganizationMapper): OrganizationPersistenceAdapter {

    @Transactional
    override fun save(organization: Organization): UUID {
        val organizationEntity = saveOrganization(organization)
        return organizationEntity.id!!
    }

    override fun getAll(): List<Organization> {
        return organizationRepository.findAll().map { mapper.organizationEntityToModel(it) }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private fun saveOrganization(organization: Organization): OrganizationEntity {
        val organizationEntity = mapper.organizationModelToEntity(organization)
        organizationEntity.contactDetails.organizationEntity = organizationEntity
        return organizationRepository.save(organizationEntity);
    }
}
