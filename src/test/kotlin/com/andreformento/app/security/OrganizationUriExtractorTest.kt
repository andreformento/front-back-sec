package com.andreformento.app.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class OrganizationUriExtractorTest {

    private val subject = OrganizationUriExtractor()

    @Test
    fun `should extract organization id from url`() {
        val result = subject.getOrganizationId("/api/organizations/4e9ec7d9-169b-11ed-b680-0242acf00302")
        assertThat(result).isEqualTo(UUID.fromString("4e9ec7d9-169b-11ed-b680-0242acf00302"))
    }

    @Test
    fun `should extract organization id from complex url`() {
        val result = subject.getOrganizationId("/api/organizations/4e9ec7d9-169b-11ed-b680-0242acf00302/blah")
        assertThat(result).isEqualTo(UUID.fromString("4e9ec7d9-169b-11ed-b680-0242acf00302"))
    }

    @Test
    fun `should not extract organization id when it isn't a organization url`() {
        val result = subject.getOrganizationId("/api/users/4e9ec7d9-169b-11ed-b680-0242acf00302")
        assertThat(result).isNull()
    }

    @Test
    fun `should not extract organization id for organization base url`() {
        val result = subject.getOrganizationId("/api/organizations")
        assertThat(result).isNull()
    }

}
