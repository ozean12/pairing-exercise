package io.billie.organisations.mapper

import io.billie.organisations.dto.OrganisationResponse
import io.billie.organisations.model.Organisation

fun Organisation.toOrganisationResponse(): OrganisationResponse =
        OrganisationResponse(
                id = this.id,
                name = this.name,
                dateFounded = this.dateFounded,
                country = this.country,
                VATNumber = this.VATNumber,
                registrationNumber = this.registrationNumber,
                legalEntityType = this.legalEntityType,
                contactDetails = this.contactDetails
        )