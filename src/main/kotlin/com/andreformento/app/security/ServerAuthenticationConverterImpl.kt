package com.andreformento.app.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*


@Component
class ServerAuthenticationConverterImpl(
    private val tokenProvider: TokenProvider,
    private val organizationUriExtractor: OrganizationUriExtractor,
) :
    ServerAuthenticationConverter {
    private fun getJwtFromRequest(headers: HttpHeaders): String? {
        val tokenHeader = headers.getFirst(TOKEN_HEADER)
        return if (StringUtils.hasText(tokenHeader) && tokenHeader!!.startsWith(TOKEN_PREFIX)) {
            tokenHeader.replace(TOKEN_PREFIX, "")
        } else null
    }

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        val request = exchange.request
        val organizationId: UUID? = organizationUriExtractor.getOrganizationId(request.uri.toString())
        val auth = getJwtFromRequest(request.headers)?.let { tokenProvider.createOAuth2AuthenticationToken(it, organizationId) }
        return Mono.justOrEmpty(auth)
    }

    companion object {
        const val TOKEN_HEADER = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
    }
}
