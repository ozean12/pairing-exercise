# Candidate Notes & Assumptions

In this repository, I have chosen to implement the task of adding an address to the organisation.

Please note that I have faced continuous issues with the database migrations using the Flyway plugin. I have tried to fix them for about 2 hours, but I was not able to do so. Despite this, I have decided to continue with the task and configured Flyway to be executed when the Spring application starts. This is sub-optimal, but I didn't want to spend too much time figuring out why each migration execution had a different result on my database. Moreover, it seems that the configuration of Flyway missed the database and therefore needed to be updated to run the migrations with the plugin.

## Notes

- The swagger documentation does not work for the `POST` endpoint of organisations. The issue is caused by a different date format between the documentation (`yyyy-MM-dd`) and the required format on the service (`dd/MM/yyyy`).
- The [Fixtures](src/test/kotlin/io/billie/functional/data/Fixtures.kt) class was completely refactored to use a string block, as it was very hard to maintain the test data in the previous format.
- The [CanStoreAndReadOrganisationTest](src/test/kotlin/io/billie/functional/CanStoreAndReadOrganisationTest.kt) was refactored to reduce repetitive code of storage rejection by using the `@ParameterizedTest` annotation of jUnit5. Moreover, all test functions have been refactored to use the Kotlin standard naming instead of the camel case (Java) standard.
- A typo in [README.md](README.md) was fixed where the instructions to run the application were pointing to the user's global Gradle instead of the project Gradle wrapper.
- Gradle wrapper and Kotlin have been upgraded to their latest versions as I struggled to run Gradle 7 on my machine properly due to Apple's M1 processor.
- [OrganisationResponse.kt](src/main/kotlin/io/billie/organisations/viewmodel/OrganisationResponse.kt) and [OrganisationRequest.kt](src/main/kotlin/io/billie/organisations/viewmodel/OrganisationRequest.kt) do not need the `@Table` annotation, as they are not database entities. We're not using Hibernate, and even if we did, we should separate responsibilities as mentioned elsewhere in this file.

## Assumptions

- Countries and Cities tables are not expected to grow significantly. Otherwise, those tables would need proper indexing.
- Address is required in any new request coming to the API; otherwise, the request is rejected.
- A new API version could be provided with this change, as there is a breaking change by using the `country code` stored in the address instead of at the top-level of the request. If such a change is not possible, there can be duplication of information with a validation that both fields are the same.
- The new address id column is nullable, as we do not have the required information to populate it for existing organisations in the production database. There are alternatives to solve this issue, but for the simplicity of this task, I decided to skip them.
- The address object is required in [ContactDetailsRequest](src/main/kotlin/io/billie/organisations/viewmodel/ContactDetailsRequest.kt). This is done under the above assumption that this change contains a new API version to not break older clients.

## What can be next?

As I didn't want to spend too much time on this exercise, here is a list of suggestions for improvements that can be done on the project:

- Integration tests should leverage a test database so that the tests are not dependent on the local database.
- All classes should use constructor injection instead of field injection and become immutable. (For example, see [OrganisationRepository.kt](src/main/kotlin/io/billie/organisations/data/OrganisationRepository.kt))
- All tests should leverage the `@TestConstructor(autowireMode = AutowireMode.ALL)` annotation, which allows constructor injection in tests. (For more details, see [CanStoreAndReadOrganisationTest.kt](src/test/kotlin/io/billie/functional/CanStoreAndReadOrganisationTest.kt))
- The current implementation uses exceptions for business logic errors. This is not ideal, as exceptions are expensive to create and throw. A better approach would be to use `Result` or `Either` types that can represent success or failure.
- Currently, the repositories contain business logic. The repository should be as simple as possible and only handle data access. The business logic should be moved to a service layer.
- There should be a separation between the request object and the object stored in the database. This is to avoid leaking implementation details to the client.
- Replace the usage of JdbcTemplate with `JOOQ`. JOOQ is a library that generates type-safe SQL queries at compile time. This allows catching errors at compile time instead of runtime. Alternatively, another option would be to use named parameters instead of positional parameters to reduce some of the risks.
- The codebase should be upgraded to run on JVM 17 as it's the latest LTS version and would soon be replaced by JVM 21.
- The contact details id might be changed to UUID in the `organisations` table, and upon deletion of an organisation, all contact details should be cascaded as well.
- The `email` field in the `contact_details` can be annotated with `@Email` to ensure valid email value. Moreover, the field would need the `@field:Valid` annotation to be validated.
