package com.andreformento.app.security


import com.andreformento.app.exception.ProviderNotIdentifiedException
import com.andreformento.app.user.NewUser
import com.andreformento.app.user.UserService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReactiveOAuth2AuthorizedClientServiceImpl(private val userService: UserService) :
    ReactiveOAuth2AuthorizedClientService {

    override fun <T : OAuth2AuthorizedClient?> loadAuthorizedClient(
        clientRegistrationId: String?,
        principalName: String?
    ): Mono<T> {
        TODO("Not yet implemented")
    }

    override fun saveAuthorizedClient(
        authorizedClient: OAuth2AuthorizedClient,
        authentication: Authentication
    ): Mono<Void> {
        val result: Mono<Void> = mono {
            if (authentication is OAuth2LoginAuthenticationToken) {
                val principal = authentication.principal
                val email: String
                val imageUrl: String

                if (principal is OidcUser) {
                    email = principal.userInfo.email;
                    imageUrl = principal.userInfo.picture;
                } else {
                    email = principal.getAttribute("email")!!
                    imageUrl = principal.getAttribute("avatar_url")!!
                }

                val providerName = authentication.clientRegistration.registrationId
                val provider = OAuth2Provider.getByName(providerName)
                    ?: throw ProviderNotIdentifiedException(String.format("Provider %s not recognized", providerName))

                // TODO maybe can be done at ReactiveAuthenticationManagerImpl
                val userExists = userService.existsUserByEmailAndProvider(email, provider)

                if (!userExists) {
                    userService.createUser(
                        NewUser(
                            name = authentication.getName(),
                            email = email,
                            imageUrl = imageUrl,
                            provider = provider
                        )
                    )
                }
            }
            null
        }

        return result
    }

    override fun removeAuthorizedClient(clientRegistrationId: String?, principalName: String?): Mono<Void> {
        TODO("Not yet implemented")
    }

}
