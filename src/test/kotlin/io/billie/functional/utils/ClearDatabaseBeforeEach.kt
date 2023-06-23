package io.billie.functional.utils

import io.billie.organisations.data.OrganisationRepository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.junit.jupiter.SpringExtension

class ClearDatabaseBeforeEach: BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val appContext = SpringExtension.getApplicationContext(context)

        appContext.getBean(OrganisationRepository::class.java).truncateAddresses()
    }
}

private fun OrganisationRepository.truncateAddresses() {
    jdbcTemplate.execute(
        "TRUNCATE TABLE organisations_schema.addresses"
    )
}
