package io.billie.organisations.data

class UnableToFindOrganisation(organisationId: String) :
    RuntimeException("Organisation not found by id $organisationId")
