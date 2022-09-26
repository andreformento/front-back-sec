package com.andreformento.app;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;

public class AppDbContainer extends MySQLContainer<AppDbContainer> {
    private static final String APP_DATABASE_NAME = "appdb";
    private static final String APP_DATABASE_USERNAME = "root";
    private static final String APP_DATABASE_PASSWORD = "secret";
    private static final String IMAGE_VERSION = "mysql:8.0.29";


    private static AppDbContainer container;

    private AppDbContainer() {
        super(IMAGE_VERSION);
    }

    public static AppDbContainer getInstance() {
        if (container == null) {
            try (AppDbContainer appDbContainer = new AppDbContainer()
                    .withClasspathResourceMapping("migrations", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
                    .withDatabaseName(APP_DATABASE_NAME)
                    .withUsername(APP_DATABASE_USERNAME)
                    .withPassword(APP_DATABASE_PASSWORD)
            ) {
                container = appDbContainer;
                container.start();
            }

        }
        return container;
    }

    @Override
    public void start() {
        super.start();
////        System.setProperty("DB_URL", appDbMysqlContainer.getJdbcUrl());
////        System.setProperty("DB_USERNAME", appDbMysqlContainer.getUsername());
////        System.setProperty("DB_PASSWORD", appDbMysqlContainer.getPassword());
//
//
//        System.setProperty("MYSQL_HOST", appDbMysqlContainer.getContainerIpAddress());
//        System.setProperty("MYSQL_PORT", appDbMysqlContainer.getFirstMappedPort().toString());
//        System.setProperty("MYSQL_DATABASE_NAME", APP_DATABASE_NAME);
//        System.setProperty("MYSQL_USERNAME", APP_DATABASE_USERNAME);
//        System.setProperty("MYSQL_PASSWORD", APP_DATABASE_PASSWORD);
//
//        System.setProperty("GITHUB_CLIENT_ID", "github_test_client_id");
//        System.setProperty("GITHUB_CLIENT_SECRET", "github_test_client_secret");
//        System.setProperty("GOOGLE_CLIENT_ID", "google_test_client_id");
//        System.setProperty("GOOGLE_CLIENT_SECRET", "google_test_client_secret");
//
//        System.setProperty("management.server.port", "8080");
//
//        System.setProperty("management.endpoint.health.show-details", "always");
//        System.setProperty("management.endpoint.health.probes.enabled", "true");
//        System.setProperty("management.endpoint.health.group.readiness.include", "databaseContentCheck");
//        System.setProperty("management.endpoints.web.exposure.include", "info,metrics,health,prometheus");
//        System.setProperty("management.metrics.tags.application", "app");
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

    public void configure(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", appDbContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", appDbContainer::getUsername);
//        registry.add("spring.datasource.password", appDbContainer::getPassword);


        //        System.setProperty("DB_URL", appDbMysqlContainer.getJdbcUrl());
//        System.setProperty("DB_USERNAME", appDbMysqlContainer.getUsername());
//        System.setProperty("DB_PASSWORD", appDbMysqlContainer.getPassword());


        registry.add("SPRING_DATASOURCE_URL", this::getJdbcUrl);
        registry.add("SPRING_DATASOURCE_USERNAME", () -> APP_DATABASE_USERNAME);
        registry.add("SPRING_DATASOURCE_PASSWORD", () -> APP_DATABASE_PASSWORD);

        registry.add("MYSQL_HOST", this::getContainerIpAddress);
        registry.add("MYSQL_PORT", this::getFirstMappedPort);
        registry.add("MYSQL_DATABASE_NAME", () -> APP_DATABASE_NAME);
        registry.add("MYSQL_USERNAME", () -> APP_DATABASE_USERNAME);
        registry.add("MYSQL_PASSWORD", () -> APP_DATABASE_PASSWORD);

        registry.add("GITHUB_CLIENT_ID", () -> "github_test_client_id");
        registry.add("GITHUB_CLIENT_SECRET", () -> "github_test_client_secret");
        registry.add("GOOGLE_CLIENT_ID", () -> "google_test_client_id");
        registry.add("GOOGLE_CLIENT_SECRET", () -> "google_test_client_secret");

        registry.add("management.server.port", () -> "8080");

        registry.add("management.endpoint.health.show-details", () -> "always");
        registry.add("management.endpoint.health.probes.enabled", () -> "true");
        registry.add("management.endpoint.health.group.readiness.include", () -> "databaseContentCheck");
        registry.add("management.endpoints.web.exposure.include", () -> "info,metrics,health,prometheus");
        registry.add("management.metrics.tags.application", () -> "app");
    }
}
