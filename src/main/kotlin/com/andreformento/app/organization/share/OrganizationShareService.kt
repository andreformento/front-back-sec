package com.andreformento.app.organization.share

import com.andreformento.app.organization.Organization
import com.andreformento.app.security.LoadedOrganizationShare
import com.andreformento.app.security.LoadedUser
import com.andreformento.app.security.OAuth2Provider
import com.andreformento.app.util.asBytes
import com.andreformento.app.util.asUUID
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganizationShareService(
    private val organizationShareRepository: OrganizationShareRepository,
    private val organizationShareFullRepository: OrganizationShareFullRepository,
) {
    suspend fun initializeOwner(loadedUser: LoadedUser, organizationId: UUID) = organizationShareRepository.save(
        OrganizationShareEntity(
            id = UUID.randomUUID().asBytes(),
            userId = loadedUser.user.id.asBytes(),
            organizationId = organizationId.asBytes(),
            role = Role.OWNER,
        )
    )

    suspend fun getByOrganizationAndUser(
        organizationId: UUID,
        userProvider: OAuth2Provider,
        userEmail: String,
    ) = organizationShareFullRepository.getByUserAndOrganization(
        organizationId = organizationId.asBytes(),
        userProvider = userProvider.name,
        userEmail = userEmail,
    )?.toModel()

    suspend fun share(
        loadedOrganizationShare: LoadedOrganizationShare,
        newOrganizationShare: NewOrganizationShare,
    ): OrganizationShared? {
        if (!loadedOrganizationShare.canShareWithUser(newOrganizationShare)) return null

        return organizationShareRepository
            .save(
                OrganizationShareEntity(
                    id = UUID.randomUUID().asBytes(),
                    userId = newOrganizationShare.user.id.asBytes(),
                    organizationId = loadedOrganizationShare.organizationShare.organization.id.asBytes(),
                    role = Role.ADMIN,
                )
            ).let {
                OrganizationShared(it.id!!.asUUID(), loadedOrganizationShare.organizationShare.organization, it.role!!)
            }
    }

    suspend fun delete(organizationShare: OrganizationShare) =
        organizationShareRepository.deleteById(organizationShare.id.asBytes())
    suspend fun deleteByOrganization(organization: Organization) =
        organizationShareRepository.deleteByOrganizationId(organization.id.asBytes())

}
