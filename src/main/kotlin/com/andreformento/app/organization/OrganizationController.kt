package com.andreformento.app.organization

import com.andreformento.app.config.SwaggerConfig
import com.andreformento.app.security.LoadedOrganizationShare
import com.andreformento.app.security.LoadedUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api/organizations")
class OrganizationController(private val organizationService: OrganizationService) {

    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @GetMapping
    suspend fun getByUser(
        @AuthenticationPrincipal loadedUser: LoadedUser,
        @RequestParam(value = "text", required = false) text: String?,
    ): List<OrganizationResponse> = organizationService
        .getAllByUser(loadedUser, text)
        .map(::OrganizationResponse)

    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @PostMapping
    suspend fun create(
        @AuthenticationPrincipal loadedUser: LoadedUser,
        @RequestBody organizationRequest: OrganizationRequest,
    ) = organizationService
        .create(loadedUser, organizationRequest)
        .let(::OrganizationResponse)
        .let {
            ResponseEntity
                .created(URI.create("/api/organizations/${it.id}"))
                .body(it)
        }
    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @PutMapping("/{id}")
    suspend fun update(
        @AuthenticationPrincipal loadedOrganizationShare: LoadedOrganizationShare,
        @RequestBody organizationRequest: OrganizationRequest,
    ) = organizationService
        .update(loadedOrganizationShare, organizationRequest)
        .let(::OrganizationResponse)
        .let { ResponseEntity.ok(it) }

    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @GetMapping("/{id}")
    suspend fun getById(
        @AuthenticationPrincipal loadedOrganizationShare: LoadedOrganizationShare,
        @PathVariable id: UUID,
    ) = loadedOrganizationShare.organizationShare.organization.toResponse()

    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun delete(
        @AuthenticationPrincipal loadedOrganizationShare: LoadedOrganizationShare,
        @PathVariable id: UUID,
    ) {
        organizationService.delete(loadedOrganizationShare)
    }
}

class OrganizationResponse(organization: Organization) {
    val id: String
    val name: String

    init {
        id = organization.id.toString()
        name = organization.name
    }
}

fun Organization.toResponse() = OrganizationResponse(this)
