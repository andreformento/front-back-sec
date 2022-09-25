package com.andreformento.app.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebFluxSecurity
class WebSecurityConfig(
    private val serverAuthenticationSuccessHandler: ServerAuthenticationSuccessHandler,
    private val serverAuthenticationConverter: ServerAuthenticationConverter,
    private val reactiveAuthenticationManager: ReactiveAuthenticationManager,
    private val reactiveOAuth2AuthorizedClientService: ReactiveOAuth2AuthorizedClientService,
    @param:Value("\${app.cors.allowed-origins}") private val allowedOrigins: List<String>
) {
    private fun corsFilter(): CorsWebFilter {
        val config = CorsConfiguration()
        config.applyPermitDefaultValues()
        config.allowCredentials = true
        config.allowedOriginPatterns = allowedOrigins
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsWebFilter(source)
    }

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(reactiveAuthenticationManager).apply {
            setServerAuthenticationConverter(serverAuthenticationConverter)
        }

        return http
            .authorizeExchange()
            .pathMatchers("/auth/**", "/oauth2/**").permitAll()
            .pathMatchers("/index.html", "/", "/home", "/login").permitAll()
            .pathMatchers("/profile", "/adminpage", "/userpage").permitAll()
            .pathMatchers("/index*", "/static/**", "/*.js", "/*.json", "/*.ico", "/*.png",).permitAll()
            .pathMatchers(
                "/",
                "/error",
                "/csrf",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**"
            ).permitAll()
            .pathMatchers(HttpMethod.GET, "/api/users/me").hasAnyAuthority(ADMIN, ROLE_USER)
            .pathMatchers("/api/organizations", "/api/organizations/**").hasAnyAuthority(ADMIN, ROLE_USER)
            .pathMatchers("/api/users", "/api/users/**").hasAnyAuthority(ADMIN)
            .anyExchange().authenticated()
            .and()
            .oauth2Login()
            .authorizedClientService(reactiveOAuth2AuthorizedClientService)
            .authenticationSuccessHandler(serverAuthenticationSuccessHandler)
        //        .logout(l -> l.logoutSuccessUrl("/").permitAll());
            .and()
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
        //        .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .build()
    }

    companion object {
        const val ADMIN = "ADMIN"
        const val ROLE_USER = "ROLE_USER"
    }
}
