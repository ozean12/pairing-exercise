package io.billie.organization_management.organisations.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ContactDetailRepository : JpaRepository<ContactDetail, UUID>
