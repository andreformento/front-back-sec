package com.andreformento.app.organization

import com.andreformento.app.configureProperties
import com.andreformento.app.createContainer
import com.andreformento.app.security.SecuritySessionTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import kotlin.random.Random


@SpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
class OrganizationControllerTest {

    @Autowired
    private lateinit var securitySessionTest: SecuritySessionTest

    @Test
    fun `should create an organization`() {
        with(securitySessionTest.createContext()) {
            webClient
                .post()
                .uri("/api/organizations")
                .bodyValue(OrganizationRequest(name = "my org test"))
                .withUser()
                .exchange()
                .expectStatus().isCreated
                .expectHeader().valueMatches("location", "/api/organizations/.*")
                .expectBody()
                .jsonPath("\$.name").isEqualTo("my org test")
        }

    }

    @Test
    fun `should filter organizations by name`() {
        val organizationName = "org_${Random.nextInt(Int.MAX_VALUE)}"

        // create an organization from other user with the same name
        with(securitySessionTest.createContext()) {
            createOrganization(organizationName)
        }

        with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization(organizationName)

            webClient
                .get()
                .uri("/api/organizations?text=${createdOrganization.name}")
                .withUser()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("\$.length()").isEqualTo(1)
                .jsonPath("\$.[:1].id").isEqualTo(createdOrganization.id)
                .jsonPath("\$.[:1].name").isEqualTo(createdOrganization.name)
        }

    }

    @Test
    fun `should filter find all organizations from user`() {
        // create an organization from other user to validate security
        with(securitySessionTest.createContext()) {
            createOrganization()
        }

        with(securitySessionTest.createContext()) {
            val createdOrganization1 = createOrganization("organization a")
            val createdOrganization2 = createOrganization("organization b")

            webClient
                .get()
                .uri("/api/organizations")
                .withUser()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("\$.length()").isEqualTo(2)
                .jsonPath("\$.[0:1].id").isEqualTo(createdOrganization1.id)
                .jsonPath("\$.[0:1].name").isEqualTo(createdOrganization1.name)
                .jsonPath("\$.[1:2].id").isEqualTo(createdOrganization2.id)
                .jsonPath("\$.[1:2].name").isEqualTo(createdOrganization2.name)
        }

    }

    @Test
    fun `should not get organization from other user`() {
        // create an organization from other user to validate security
        val organizationFromOtherUser = securitySessionTest.createContext().createOrganization()

        with(securitySessionTest.createContext()) {
            webClient
                .get()
                .uri("/api/organizations/${organizationFromOtherUser.id}")
                .withUser()
                .exchange()
                .expectStatus().isNotFound
        }

    }

    @Test
    fun `owner should delete single organization`() {
        with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization()
            val organizationUri = "/api/organizations/${createdOrganization.id}"

            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isAccepted

            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isNotFound
        }
    }

    @Test
    fun `owner should delete all shared organization`() {
        val adminUser = securitySessionTest.createContext().user

        val organizationUri = with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization()

            shareOrganization(createdOrganization.id, adminUser.id)

            val organizationUri = "/api/organizations/${createdOrganization.id}"

            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isAccepted

            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isNotFound

            return@with organizationUri
        }

        with(securitySessionTest.getContext(adminUser)) {
            webClient
                .get()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isNotFound
        }

    }

    @Test
    fun `admin should delete your own shared organization`() {
        val adminUser = securitySessionTest.createContext().user

        // owner create organization
        val (organizationUri, ownerUser) = with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization()

            shareOrganization(createdOrganization.id, adminUser.id)

            val organizationUri = "/api/organizations/${createdOrganization.id}"

            return@with Pair(organizationUri, user)
        }

        // admin can delete only shared organization
        with(securitySessionTest.getContext(adminUser)) {
            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isAccepted

            webClient
                .get()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isNotFound
        }

        // owner need to see organization yet
        with(securitySessionTest.getContext(ownerUser)) {
            webClient
                .get()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isOk
        }
    }

    @Test
    fun `should not delete organization from other user`() {
        // create an organization from other user to validate security
        val (organizationFromOtherUser, otherUser) = with(securitySessionTest.createContext()) {
            Pair(createOrganization(), user)
        }
        val organizationUri = "/api/organizations/${organizationFromOtherUser.id}"

        with(securitySessionTest.createContext()) {
            webClient
                .delete()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isNotFound
        }

        with(securitySessionTest.getContext(otherUser)) {
            webClient
                .get()
                .uri(organizationUri)
                .withUser()
                .exchange()
                .expectStatus().isOk
        }
    }

    @Test
    fun `should update organization`() {
        with(securitySessionTest.createContext()) {
            val createdOrganization = createOrganization("new org")

            webClient
                .put()
                .uri("/api/organizations/${createdOrganization.id}")
                .bodyValue(OrganizationRequest(name = "updated org"))
                .withUser()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("\$.name").isEqualTo("updated org")
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