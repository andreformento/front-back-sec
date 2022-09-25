package com.andreformento.app.organization.share


import com.andreformento.app.config.SwaggerConfig
import com.andreformento.app.organization.OrganizationResponse
import com.andreformento.app.security.LoadedOrganizationShare
import com.andreformento.app.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

data class NewOrganizationShareRequest(val userId: UUID)

class OrganizationShareResponse(organizationShared: OrganizationShared) {
    val id: UUID = organizationShared.id
    val organization: OrganizationResponse = OrganizationResponse(organizationShared.organization)
    val role: Role = organizationShared.role
}

@RestController
@RequestMapping("/api/organizations/{organization-id}/shares")
class OrganizationShareController(
    private val organizationShareService: OrganizationShareService,
    private val userService: UserService,
) {

    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @PostMapping
    suspend fun share(
        @AuthenticationPrincipal loadedOrganizationShare: LoadedOrganizationShare,
        @PathVariable("organization-id") organizationParam: String,
        @RequestBody newOrganizationShareRequest: NewOrganizationShareRequest,
    ): ResponseEntity<OrganizationShareResponse> {
        val user = userService.getById(newOrganizationShareRequest.userId) ?: return ResponseEntity.notFound().build()

        return organizationShareService
            .share(loadedOrganizationShare, NewOrganizationShare(user))
            ?.let { OrganizationShareResponse(it) }
            ?.let {
                ResponseEntity.created(URI.create("/api/organizations/${it.organization.id}/shares/${it.id}")).body(it)
            }
            ?: ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
}
