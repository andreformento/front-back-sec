package com.andreformento.app.security

import org.springframework.stereotype.Component
import java.util.*

@Component
class OrganizationUriExtractor {
    companion object {
        private const val baseOrganizationUri = "/api/organizations/"
        private val prefixUriRegex = "$baseOrganizationUri.+".toRegex()
        private val sufixUriRegex = "/.*".toRegex()
    }

    fun getOrganizationId(uri: String): UUID? =
        prefixUriRegex
            .find(uri)
            ?.takeIf { it.groupValues.isNotEmpty() }
            ?.let { it.groupValues[0] }
            ?.replace(baseOrganizationUri, "")
            ?.replace(sufixUriRegex, "")
            ?.let(UUID::fromString)
}
