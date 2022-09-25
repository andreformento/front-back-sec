package com.andreformento.app.organization.share

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OrganizationShareRepository : CoroutineCrudRepository<OrganizationShareEntity, ByteArray> {
    @Modifying
    suspend fun deleteByOrganizationId(@Param("organization_id") organizationId: ByteArray)
}

@Repository
interface OrganizationShareFullRepository : CoroutineCrudRepository<OrganizationShareFullEntity, ByteArray> {
    @Query(
        """
        select os.id as organization_share_id, os.`role`, os.organization_id, os.user_id,
               u.name user_name, u.email user_email, u.image_url user_image_url, u.provider user_provider,
               o.name organization_name
          from organization_share os
    inner join users u on u.id = os.user_id 
    inner join organization o on o.id = os.organization_id 
         where o.id       = :organization_id 
           and u.email    = :user_email 
           and u.provider = :user_provider  
    """
    )
    suspend fun getByUserAndOrganization(
        @Param("organization_id") organizationId: ByteArray,
        @Param("user_email") userEmail: String,
        @Param("user_provider") userProvider: String,
    ): OrganizationShareFullEntity?

}
