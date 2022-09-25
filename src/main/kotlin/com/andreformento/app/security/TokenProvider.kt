package com.andreformento.app.security

import com.andreformento.app.exception.AuthenticationException
import com.andreformento.app.exception.ProviderNotIdentifiedException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.util.*

@Component
class TokenProvider(
    @Value("\${app.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.jwt.expiration.minutes}") private val jwtExpirationMinutes: Long,
) {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(TokenProvider::class.java)

        private const val TOKEN_TYPE: String = "JWT"
        private const val TOKEN_ISSUER: String = "api"
        private const val TOKEN_AUDIENCE: String = "app"
    }

    data class UserAuth(
        val authorities: List<GrantedAuthority>,
        val name: String,
        val provider: OAuth2Provider,
        val email: String,
    )

    data class UserToken(val userAuth: UserAuth, val token: String)

    fun generate(userAuth: UserAuth): UserToken {
        val roles = userAuth.authorities.map { it.authority }

        val signingKey = jwtSecret.toByteArray(StandardCharsets.UTF_8)

        val token = Jwts
            .builder()
            .setHeaderParam("typ", TOKEN_TYPE)
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationMinutes).toInstant()))
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setId(UUID.randomUUID().toString())
            .setIssuer(TOKEN_ISSUER)
            .setAudience(TOKEN_AUDIENCE)
            .setSubject(userAuth.email)
            .claim("rol", roles)
            .claim("name", userAuth.name)
            .claim("provider", userAuth.provider)
            .compact()
        return UserToken(
            userAuth = userAuth,
            token = token,
        )
    }

    fun generate(authentication: Authentication): UserToken {
        val provider = if (authentication is OAuth2AuthenticationToken) {
            val providerName = authentication.authorizedClientRegistrationId
            OAuth2Provider.getByName(providerName)
                ?: throw ProviderNotIdentifiedException(String.format("Provider %s not recognized", providerName))
        } else {
            throw ProviderNotIdentifiedException("Provider cannot be recognized")
        }

        val principal = authentication.principal

        return if (principal is DefaultOAuth2User) {
            val userAuth = UserAuth(
                authorities = principal.getAuthorities().toList(),
                name = principal.getAttribute("name")!!,
                provider = provider,
                email = principal.getAttribute("email")!!
            )

            generate(userAuth)
        } else {
            throw AuthenticationException("Authentication principal wrong")
        }
    }

    fun createOAuth2AuthenticationToken(token: String, organizationId: UUID?): OAuth2AuthenticationToken? {
        val jws: Jws<Claims> = validateTokenAndGetJws(token) ?: return null

        val email: String = jws.body.subject
        val providerName: String = jws.body.get("provider", String::class.java)
        val roles: List<String> = jws.body.get("rol", ArrayList::class.java).toList().map { it.toString() }
        val name: String = jws.body.get("name", String::class.java)
        val provider: OAuth2Provider = OAuth2Provider.getByName(providerName)
            ?: throw ProviderNotIdentifiedException(String.format("Provider %s not recognized", providerName))

        val authorities = AuthorityUtils.createAuthorityList(*roles.toTypedArray())
        val authenticatedUser = AuthenticatedUser(name, authorities, emptyMap(), email, provider, organizationId)
        return OAuth2AuthenticationToken(authenticatedUser, authorities, providerName)
    }

    // TODO why public?
    fun validateTokenAndGetJws(token: String): Jws<Claims>? {
        try {
            val signingKey = jwtSecret.toByteArray(StandardCharsets.UTF_8)

            return Jwts
                .parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
        } catch (exception: ExpiredJwtException) {
            LOGGER.error("Request to parse expired JWT : {} failed : {}", token, exception.message)
        } catch (exception: UnsupportedJwtException) {
            LOGGER.error("Request to parse unsupported JWT : {} failed : {}", token, exception.message)
        } catch (exception: MalformedJwtException) {
            LOGGER.error("Request to parse invalid JWT : {} failed : {}", token, exception.message)
        } catch (exception: SecurityException) {
            LOGGER.error("Request to parse JWT with invalid signature : {} failed : {}", token, exception.message)
        } catch (exception: IllegalArgumentException) {
            LOGGER.error("Request to parse empty or null JWT : {} failed : {}", token, exception.message)
        }
        return null
    }

}
