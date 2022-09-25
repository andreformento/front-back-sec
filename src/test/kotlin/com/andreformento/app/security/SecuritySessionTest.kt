package com.andreformento.app.security

import com.andreformento.app.organization.OrganizationRequest
import com.andreformento.app.organization.OrganizationResponse
import com.andreformento.app.organization.share.NewOrganizationShareRequest
import com.andreformento.app.organization.share.OrganizationShareResponse
import com.andreformento.app.user.NewUser
import com.andreformento.app.user.User
import com.andreformento.app.user.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
import kotlin.random.Random

fun createAnEmail() = "eddie${Random.nextInt(Int.MAX_VALUE)}@test.com"

data class SecuritySessionContext(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    val userToken: TokenProvider.UserToken,
    val user: User,
    val webClient: WebTestClient,
    val email: String,
) {

    fun <S : WebTestClient.RequestHeadersSpec<S>> WebTestClient.RequestHeadersSpec<S>.withUser(): WebTestClient.RequestHeadersSpec<S> =
        this.headers { it.setBearerAuth(userToken.token) }

    fun createOrganization(name: String = "new organization created") =
        webClient
            .post()
            .uri("/api/organizations")
            .bodyValue(OrganizationRequest(name = name))
            .withUser()
            .exchange()
            .expectStatus().isCreated
            .returnResult(OrganizationResponse::class.java)
            .responseBody
            .blockFirst()!!

    fun shareOrganization(organizationId: String, userIdToShare: UUID) =
        webClient
            .post()
            .uri("/api/organizations/${organizationId}/shares")
            .bodyValue(NewOrganizationShareRequest(userIdToShare))
            .withUser()
            .exchange()
            .expectStatus().isCreated
            .returnResult(OrganizationShareResponse::class.java)
            .responseBody
            .blockFirst()!!

}

@Component
class SecuritySessionTest(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    private val webClient: WebTestClient,
) {

    fun createContext(email: String = createAnEmail()) = saveUser(email).let(this::getContext)
    fun getContext(user: User) =
        SecuritySessionContext(
            tokenProvider = tokenProvider,
            userService = userService,
            userToken = generateUserToken(user),
            user = user,
            webClient = webClient,
            email = user.email,
        )


    private fun saveUser(email: String) =
        runBlocking {
            userService.createUser(
                NewUser(
                    name = "Eddie",
                    email = email,
                    imageUrl = "",
                    provider = OAuth2Provider.GOOGLE,
                )
            )
        }

    private fun generateUserToken(user: User): TokenProvider.UserToken = tokenProvider.generate(
        TokenProvider.UserAuth(
            authorities = listOf(SimpleGrantedAuthority("ROLE_USER")),
            name = user.name,
            provider = user.provider,
            email = user.email,
        )
    )

}
