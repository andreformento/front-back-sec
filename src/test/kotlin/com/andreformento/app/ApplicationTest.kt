package com.andreformento.app

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.junit.jupiter.Container

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
@ExtendWith(SpringExtension::class, MockitoExtension::class)
class ApplicationTest {

    @Test
    fun contextLoads() {
    }

    companion object {
        @Container
        private val databaseContainer = createContainer()

        @DynamicPropertySource
        @JvmStatic
        fun properties(registry: DynamicPropertyRegistry) = registry.configureProperties(databaseContainer)
    }

}

