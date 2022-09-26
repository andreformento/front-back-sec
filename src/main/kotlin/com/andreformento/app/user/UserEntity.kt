package com.andreformento.app.user

import com.andreformento.app.security.OAuth2Provider
import com.andreformento.app.util.BaseView
import com.andreformento.app.util.asBytes
import com.andreformento.app.util.asUUID
import org.springframework.data.annotation.Id
import java.util.*
import javax.persistence.Table

@Table(name = "users")
data class UserEntity(
    @Id
    private var id: ByteArray? = UUID.randomUUID().asBytes(),
    var name: String? = null,
    var email: String? = null,
    var imageUrl: String? = null,
    var provider: OAuth2Provider? = null,
) : BaseView<ByteArray>(id) {

    fun toModel() = User(
        id = this.id!!.asUUID(),
        name = name!!,
        email = email!!,
        imageUrl = imageUrl!!,
        provider = provider!!,
    )

    override fun getId(): ByteArray? = this.id

}

fun NewUser.fromModel() = UserEntity(
    id = UUID.randomUUID().asBytes(),
    name = name,
    email = email,
    imageUrl = imageUrl,
    provider = provider,
).apply { markNew() }
