package com.andreformento.app.user;

import com.andreformento.app.AppDbContainer;
import com.andreformento.app.SessionContextTest;
import com.andreformento.app.Util;
import com.andreformento.app.security.SecuritySessionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "3600000")
@ExtendWith(value = SpringExtension.class)
@ExtendWith(value = MockitoExtension.class)
@Testcontainers
public class UserControllerTest {

//    @ClassRule
    private static AppDbContainer appDbContainer = AppDbContainer.getInstance();

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        appDbContainer.configure(registry);
    }

    @Autowired
    private SecuritySessionTest securitySessionTest;

    @Test
    public void shouldGetUserInformation() {
        // TODO use EMAIL default here
        final SessionContextTest context = securitySessionTest.createContext(Util.createAnEmail());

        context.webClient()
                .get()
                .uri("/api/users/me")
//                .withUser()
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(context.user().getId().toString())
                .jsonPath("name").isEqualTo(context.user().getName())
                .jsonPath("email").isEqualTo(context.user().getEmail());

    }

}
