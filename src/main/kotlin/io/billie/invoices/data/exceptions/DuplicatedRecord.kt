package io.billie.invoices.data.exceptions

import java.util.UUID

class DuplicatedRecord(val id: UUID) : RuntimeException()