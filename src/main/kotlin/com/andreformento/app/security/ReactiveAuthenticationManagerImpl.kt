package com.andreformento.app.security

import com.andreformento.app.exception.OrganizationNotFoundException
import com.andreformento.app.organization.share.OrganizationShareService
import com.andreformento.app.user.UserService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class ReactiveAuthenticationManagerImpl(
    private val userService: UserService,
    private val organizationShareService: OrganizationShareService,
) : ReactiveAuthenticationManager {

    private suspend fun getUser(principal: AuthenticatedUser) = userService
        .getUserByEmailAndProvider(principal.email, principal.provider)
        ?.let {
            OAuth2AuthenticationToken(
                principal.toLoadedUser(it),
                principal.authorities,
                principal.provider.name
            )
        }

    private suspend fun getUserWithOrganization(principal: AuthenticatedUser, organizationId: UUID) =
        organizationShareService
            .getByOrganizationAndUser(
                userEmail = principal.email,
                userProvider = principal.provider,
                organizationId = organizationId,
            )?.let {
                OAuth2AuthenticationToken(
                    principal.toLoadedOrganizationShare(it),
                    principal.authorities,
                    principal.provider.name
                )
            }?: throw OrganizationNotFoundException("Organization '$organizationId' not found")

    private suspend fun getAuthentication(
        principal: AuthenticatedUser,
        organizationId: UUID?
    ): OAuth2AuthenticationToken? = when (organizationId) {
        is UUID -> getUserWithOrganization(principal, organizationId)
        else -> getUser(principal)
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        authentication.principal.let { principal ->
            mono {
                when (principal) {
                    is AuthenticatedUser -> getAuthentication(principal, principal.organizationId)
                    else -> null
                }

            }
        }
}
