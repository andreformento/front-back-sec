package com.andreformento.app.organization

import com.andreformento.app.util.BaseView
import com.andreformento.app.util.asBytes
import com.andreformento.app.util.asUUID
import org.springframework.data.annotation.Id
import java.util.*
import javax.persistence.Table

@Table(name = "organization")
data class OrganizationEntity(
    @Id
    private var id: ByteArray? = UUID.randomUUID().asBytes(),
    private var name: String? = null,
) : BaseView<ByteArray>(id) {

    fun toModel() = Organization(
        id = this.id!!.asUUID(),
        name = name!!,
    )

}

fun OrganizationRequest.updateFromModel(organizationId: UUID) = OrganizationEntity(
    id = organizationId.asBytes(),
    name = name,
)

fun OrganizationRequest.newFromModel() = updateFromModel(UUID.randomUUID()).apply { markNew() }
