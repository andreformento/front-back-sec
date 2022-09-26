package com.andreformento.app.organization

import com.andreformento.app.organization.share.OrganizationShareService
import com.andreformento.app.organization.share.Role
import com.andreformento.app.security.LoadedOrganizationShare
import com.andreformento.app.security.LoadedUser
import com.andreformento.app.util.asBytes
import com.andreformento.app.util.unwrap
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationShareService: OrganizationShareService
) {
    suspend fun getAllByUser(loadedUser: LoadedUser, text: String?): List<Organization> = (
            if (text.isNullOrEmpty())
                organizationRepository.findAllByOrderByName(loadedUser.user.id.asBytes())
            else
                organizationRepository.findByNameContainingOrderByName(loadedUser.user.id.asBytes(), text)
            )
        .map { it.toModel() }

    suspend fun getById(loadedUser: LoadedUser, id: UUID): Organization? =
        organizationRepository.findById(id.asBytes()).unwrap()?.toModel()

    suspend fun create(loadedUser: LoadedUser, organizationRequest: OrganizationRequest) = organizationRepository
        .save(organizationRequest.newFromModel()).toModel()
        .also { organizationShareService.initializeOwner(loadedUser, it.id) }

    suspend fun update(
        organizationShare: LoadedOrganizationShare,
        organizationRequest: OrganizationRequest
    ): Organization =
        organizationRepository
            .save(organizationRequest.updateFromModel(organizationShare.organizationShare.organization.id))
            .toModel()

    suspend fun delete(organizationShare: LoadedOrganizationShare) =
        if (organizationShare.organizationShare.role == Role.OWNER) {
            organizationShareService.deleteByOrganization(organizationShare.organizationShare.organization)
            organizationRepository.deleteById(organizationShare.organizationShare.organization.id.asBytes())
        } else {
            organizationShareService.delete(organizationShare.organizationShare)
        }

}
