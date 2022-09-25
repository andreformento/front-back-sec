package com.andreformento.app.organization.share

import com.andreformento.app.configureProperties
import com.andreformento.app.createContainer
import com.andreformento.app.printBody
import com.andreformento.app.security.SecuritySessionTest
import com.andreformento.app.security.createAnEmail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
class OrganizationShareControllerTest {
    @Autowired
    private lateinit var securitySessionTest: SecuritySessionTest

    @Test
    fun `should share an organization with other user`() {
        val adminUser = securitySessionTest.createContext(createAnEmail()).user
        val ownerUser = securitySessionTest.createContext(createAnEmail()).user

        with(securitySessionTest.getContext(ownerUser)) {
            val createdOrganization = createOrganization()

            webClient
                .post()
                .uri("/api/organizations/${createdOrganization.id}/shares")
                .bodyValue(NewOrganizationShareRequest(adminUser.id))
                .withUser()
                .exchange()
                .expectStatus().isCreated
                .printBody()
                .expectBody()
                .jsonPath("\$.id").isNotEmpty
                .jsonPath("\$.role").isEqualTo("ADMIN")
                .jsonPath("\$.organization.id").isEqualTo(createdOrganization.id)
                .jsonPath("\$.organization.name").isEqualTo(createdOrganization.name)
        }
    }
    @Test
    fun `should not share with yourself`() {
        with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization()

            webClient
                .post()
                .uri("/api/organizations/${createdOrganization.id}/shares")
                .bodyValue(NewOrganizationShareRequest(user.id))
                .withUser()
                .exchange()
                .expectStatus().isForbidden
                .expectBody().isEmpty
        }
    }

    companion object {
        @Container
        private val databaseContainer = createContainer()

        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) = registry.configureProperties(databaseContainer)
    }

}
