package io.billie.organisations.orders.data

import io.billie.organisations.viewmodel.Entity

/**
 * Error condition representing non-existence of organisation
 */
class OrganisationNotFoundException(organisationId: Entity) : RuntimeException("No organisation found with id " + organisationId.id)
