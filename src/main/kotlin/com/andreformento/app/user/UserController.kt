package com.andreformento.app.user

import com.andreformento.app.config.SwaggerConfig
import com.andreformento.app.security.LoadedUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    @Operation(security = [SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)])
    @GetMapping("/me")
    suspend fun getCurrentUser(@AuthenticationPrincipal loadedUser: LoadedUser) =
        UserResponse(loadedUser.user)
}

class UserResponse(user: User) {
    val id: String
    val name: String?
    val email: String?
    val imageUrl: String?

    init {
        id = user.id.toString()
        name = user.name
        email = user.email
        imageUrl = user.imageUrl
    }
}
