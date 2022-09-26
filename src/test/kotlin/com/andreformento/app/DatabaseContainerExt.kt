package com.andreformento.app

import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.MySQLContainer

private const val appDatabaseName = "appdb"
private const val appDatabaseUsername = "root"
private const val appDatabasePassword = "secret"

fun createContainer() =
    MySQLContainer("mysql:8.0.29")
        .apply {
            withClasspathResourceMapping("migrations", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
            withDatabaseName(appDatabaseName)
            withUsername(appDatabaseUsername)
            withPassword(appDatabasePassword)
            start()
        }

fun DynamicPropertyRegistry.configureProperties(
    mySQLContainer: MySQLContainer<*>,
    hostOverride: String = mySQLContainer.containerIpAddress
) {
    this.add("MYSQL_HOST") { hostOverride }
    this.add("MYSQL_PORT") { mySQLContainer.firstMappedPort }
    this.add("MYSQL_DATABASE_NAME", ::appDatabaseName)
    this.add("MYSQL_USERNAME", ::appDatabaseUsername)
    this.add("MYSQL_PASSWORD", ::appDatabasePassword)

    this.add("GITHUB_CLIENT_ID") { "github_test_client_id" }
    this.add("GITHUB_CLIENT_SECRET") { "github_test_client_secret" }
    this.add("GOOGLE_CLIENT_ID") { "google_test_client_id" }
    this.add("GOOGLE_CLIENT_SECRET") { "google_test_client_secret" }

    this.add("management.server.port") { "8080" }

    this.add("management.endpoint.health.show-details") { "always" }
    this.add("management.endpoint.health.probes.enabled") { "true" }
    this.add("management.endpoint.health.group.readiness.include") { "databaseContentCheck" }
    this.add("management.endpoints.web.exposure.include") { "info,metrics,health,prometheus" }
    this.add("management.metrics.tags.application") { "app" }
}
