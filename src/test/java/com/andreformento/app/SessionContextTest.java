package com.andreformento.app;

import com.andreformento.app.security.SecuritySessionContext;
import com.andreformento.app.user.User;
import org.springframework.test.web.reactive.server.WebTestClient;

public record SessionContextTest(WebTestClient webClient, User user, SecuritySessionContext securitySessionContext) {
}
