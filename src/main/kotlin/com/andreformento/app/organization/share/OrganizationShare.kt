package com.andreformento.app.organization.share

import com.andreformento.app.organization.Organization
import com.andreformento.app.user.User
import java.util.*

data class OrganizationShare(
    val id: UUID,
    val user: User,
    val organization: Organization,
    val role: Role,
)

enum class Role {
    OWNER,
    ADMIN,
}

data class NewOrganizationShare(val user: User)

data class OrganizationShared(
    val id: UUID,
    val organization: Organization,
    val role: Role,
)
