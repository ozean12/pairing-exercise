package io.billie.shared

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName


open class PostgresqlSharedContainer : BeforeAllCallback, AfterAllCallback {

    companion object {
        //@Container
        val postgresContainer = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13.2-alpine")).apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            // Enable Flyway and set locations for migrations
            registry.add("spring.flyway.enabled", { true })
            registry.add("spring.flyway.locations", { "classpath:db/migration" })
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        postgresContainer.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        postgresContainer.stop()
    }
}