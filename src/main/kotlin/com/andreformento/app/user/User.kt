package com.andreformento.app.user

import com.andreformento.app.security.OAuth2Provider
import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val imageUrl: String,
    val provider: OAuth2Provider,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

data class NewUser(
    val name: String,
    val email: String,
    val imageUrl: String,
    val provider: OAuth2Provider,
)
