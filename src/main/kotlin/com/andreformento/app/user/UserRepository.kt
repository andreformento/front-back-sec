package com.andreformento.app.user

import com.andreformento.app.security.OAuth2Provider
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, ByteArray> {

    suspend fun findByEmailAndProvider(email: String, provider: OAuth2Provider): UserEntity?
    suspend fun existsByEmailAndProvider(email: String, provider: OAuth2Provider): Boolean
    suspend fun findByEmailContainingOrNameContainingOrderByName(email: String, name: String): List<UserEntity>
    suspend fun findAllByOrderByName(): List<UserEntity>
}
