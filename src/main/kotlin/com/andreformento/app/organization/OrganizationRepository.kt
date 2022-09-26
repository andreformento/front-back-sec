package com.andreformento.app.organization

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : CrudRepository<OrganizationEntity, ByteArray> {

    @Query("""
        SELECT o.*
          FROM organization o
    inner join organization_share os on os.organization_id = o.id
         WHERE os.user_id = :user_id 
           AND o.name like :name
         order by o.name
    """)
    suspend fun findByNameContainingOrderByName(
        @Param("user_id") userId: ByteArray,
        @Param("name") name: String,
    ): List<OrganizationEntity>
    @Query("""
        SELECT o.*
          FROM organization o
    inner join organization_share os on os.organization_id = o.id
         WHERE os.user_id = :user_id 
         order by o.name
    """)
    suspend fun findAllByOrderByName(@Param("user_id") userId: ByteArray): List<OrganizationEntity>

}
