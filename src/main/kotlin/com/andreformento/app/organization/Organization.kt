package com.andreformento.app.organization

import java.util.*

data class Organization(
    val id: UUID,
    val name: String,
)

data class OrganizationRequest(
    val name: String,
)
