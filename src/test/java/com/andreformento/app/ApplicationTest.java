package com.andreformento.app;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class ApplicationTest {

//    @ClassRule
//    private static AppDbContainer appDbContainer = AppDbContainer.getInstance();

    private static AppDbContainer appDbContainer = AppDbContainer.getInstance();

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        appDbContainer.configure(registry);
    }

    @Test
    public void contextLoads() {
    }

}
