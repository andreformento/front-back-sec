package com.andreformento.app.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.savedrequest.ServerRequestCache
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI


@Component
class ServerAuthenticationSuccessHandlerImpl(
    private val tokenProvider: TokenProvider,
    @param:Value("\${app.oauth2.redirectUri}") private val redirectUri: String
) : ServerAuthenticationSuccessHandler {
    private val redirectStrategy: ServerRedirectStrategy
    private val requestCache: ServerRequestCache

    init {
        redirectStrategy = DefaultServerRedirectStrategy()
        requestCache = WebSessionServerRequestCache()
    }

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication
    ): Mono<Void> {
        val userToken = tokenProvider.generate(authentication)
        val targetUri = UriComponentsBuilder.fromUriString(redirectUri).queryParam("token", userToken.token).build().toUri()
        val exchange = webFilterExchange.exchange
        return requestCache.getRedirectUri(exchange).defaultIfEmpty(targetUri)
            .flatMap { location: URI? ->
                redirectStrategy.sendRedirect(
                    exchange,
                    location
                )
            }
    }
}
