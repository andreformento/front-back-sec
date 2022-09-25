package com.andreformento.app.user

import com.andreformento.app.security.OAuth2Provider
import com.andreformento.app.util.asBytes
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService(private val userRepository: UserRepository) {
    suspend fun getUsers(text: String?): List<User> = (
            if (text.isNullOrEmpty())
                userRepository.findAllByOrderByName()
            else
                userRepository.findByEmailContainingOrNameContainingOrderByName(
                    text,
                    text
                )
            )
        .map { it.toModel() }

    suspend fun getUserByEmailAndProvider(email: String, provider: OAuth2Provider): User? =
        userRepository.findByEmailAndProvider(email, provider)?.toModel()
    suspend fun existsUserByEmailAndProvider(email: String, provider: OAuth2Provider): Boolean =
        userRepository.existsByEmailAndProvider(email, provider)

    suspend fun getById(userId: UUID): User? =
        userRepository.findById(userId.asBytes())?.toModel()

    suspend fun createUser(newUser: NewUser): User = userRepository.save(newUser.fromModel()).toModel()

}
