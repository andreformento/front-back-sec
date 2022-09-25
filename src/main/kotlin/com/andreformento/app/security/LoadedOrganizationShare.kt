package com.andreformento.app.security

import com.andreformento.app.organization.share.NewOrganizationShare
import com.andreformento.app.organization.share.OrganizationShare
import com.andreformento.app.organization.share.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class LoadedOrganizationShare(
    val organizationShare: OrganizationShare,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any>
) : OAuth2User {
    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getName(): String {
        return organizationShare.user.name
    }

    fun canShareWithUser(newOrganizationShare: NewOrganizationShare): Boolean =
        when (organizationShare.role) {
            Role.OWNER -> organizationShare.user != newOrganizationShare.user
            else -> false
        }

}
