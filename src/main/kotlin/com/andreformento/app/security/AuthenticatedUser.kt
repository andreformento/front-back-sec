package com.andreformento.app.security

import com.andreformento.app.organization.share.OrganizationShare
import com.andreformento.app.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*


class AuthenticatedUser(
    private val name: String,
    private val authorities: Collection<GrantedAuthority>,
    private val attributes: Map<String, Any>,
    val email: String,
    val provider: OAuth2Provider,
    val organizationId: UUID?,
) : OAuth2User {

    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getName(): String {
        return name
    }

    fun toLoadedUser(user: User) = LoadedUser(user, authorities, attributes)
    fun toLoadedOrganizationShare(organizationShare: OrganizationShare) = LoadedOrganizationShare(organizationShare, authorities, attributes)
}
