package io.billie.organization_management.organisations.data

import java.util.*

class OrganisationNotFoundException(organisationId: UUID) : RuntimeException("Organisation with id $organisationId not found")
