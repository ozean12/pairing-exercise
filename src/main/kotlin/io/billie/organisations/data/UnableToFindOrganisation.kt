package io.billie.organisations.data

import java.util.UUID

class UnableToFindOrganisation(val organisationId: UUID): RuntimeException()