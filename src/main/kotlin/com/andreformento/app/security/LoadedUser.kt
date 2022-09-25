package com.andreformento.app.security

import com.andreformento.app.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

data class LoadedUser(
    val user: User,
    private val authorities: Collection<GrantedAuthority?>,
    private val attributes: Map<String, Any>
) : OAuth2User {
    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return authorities
    }

    override fun getName(): String {
        return user.name
    }

}
