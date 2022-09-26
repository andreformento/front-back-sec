package com.andreformento.app.organization.share

import com.andreformento.app.organization.Organization
import com.andreformento.app.security.OAuth2Provider
import com.andreformento.app.user.User
import com.andreformento.app.util.BaseView
import com.andreformento.app.util.asBytes
import com.andreformento.app.util.asUUID
import org.springframework.data.annotation.Id
import java.util.*
import javax.persistence.Column
import javax.persistence.Table

@Table(name = "organization_share")
data class OrganizationShareEntity(
    @Id
    private var id: ByteArray? = UUID.randomUUID().asBytes(),
    private var userId: ByteArray? = null,
    private var organizationId: ByteArray? = null,
    var role: Role? = null,
) : BaseView<ByteArray>(id) {
    init {
        markNew()
    }

}

@Table(name = "organization_share")
data class OrganizationShareFullEntity(
    @Id
    @Column(name="organization_share_id")
    private var id: ByteArray? = null,
    var userId: ByteArray? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userImageUrl: String? = null,
    var userProvider: OAuth2Provider? = null,
    var organizationId: ByteArray? = null,
    var organizationName: String? = null,
    var role: Role? = null,
) : BaseView<ByteArray>(organizationId) {

    fun toModel() = OrganizationShare(
        id = id!!.asUUID(),
        user = User(
            id = userId!!.asUUID(),
            name = userName!!,
            email = userEmail!!,
            imageUrl = userImageUrl!!,
            provider = userProvider!!
        ),
        organization = Organization(id = organizationId!!.asUUID(), name = organizationName!!),
        role = role!!,
    )

}
