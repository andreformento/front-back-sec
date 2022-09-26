package com.andreformento.app.user

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

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
class UserControllerTest1 {

    @Autowired
    private lateinit var securitySessionTest: SecuritySessionTest

    @Test
    fun `should get user information`() {
        with(securitySessionTest.createContextTODO()) {
            webClient
                .get()
                .uri("/api/users/me")
                .withUser()
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("id").isEqualTo(user.id.toString())
                .jsonPath("name").isEqualTo(user.name)
                .jsonPath("email").isEqualTo(user.email)
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
