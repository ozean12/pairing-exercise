package io.billie.organisations.service

import io.billie.address.service.CountryService
import io.billie.organisations.persistence.OrganizationPersistenceAdapter
import io.billie.organisations.service.model.Organization
import org.springframework.stereotype.Service
import java.util.*

@Service
class SimpleOrganizationService(private val organizationPersistenceAdapter: OrganizationPersistenceAdapter,
                                private val countryService: CountryService): OrganizationService {
    override fun add(organization: Organization): UUID {
        checkCountryCode(organization.country!!.code);
        return organizationPersistenceAdapter.save(organization);
    }

    override fun getAll(): List<Organization> {
        val organizations = organizationPersistenceAdapter.getAll()
        setCountry(organizations)
        return organizations
    }

    // -----------------------------------------------------------------------------------------------------------------

    private fun checkCountryCode(code: String) {
        countryService.getByCode(code) ?: throw IllegalArgumentException("country code does not exist")
    }

    private fun setCountry(organizations: List<Organization>) {
        organizations.listIterator().forEach { it.country = countryService.getByCode(it.country!!.code)}
    }
}
