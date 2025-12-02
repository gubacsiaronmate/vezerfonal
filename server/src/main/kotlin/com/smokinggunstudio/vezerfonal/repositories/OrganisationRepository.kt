package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.helpers.toSingle
import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.objects.Organisations
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class OrganisationRepository(val db: Database) {
    private fun ResultRow.toOrg(): Organisation = Organisation(
        id = this[Organisations.id],
        name = this[Organisations.name],
        externalId = this[Organisations.externalId],
        createdAt = this[Organisations.createdAt]
    )
    
    suspend fun getOrganisations(): List<Organisation> =
        suspendTransaction(db) {
            Organisations
                .selectAll()
                .map { it.toOrg() }
        }
    
    suspend fun getOrganisationByCondition(
        condition: SQLCondition
    ): Organisation? = suspendTransaction(db) {
        Organisations.select(condition).toSingle()?.toOrg()
    }
    
    suspend fun getOrganisationsByCondition(
        condition: SQLCondition
    ): List<Organisation> = suspendTransaction(db) {
        Organisations.select(condition).map { it.toOrg() }
    }
    
    suspend fun getOrganisationById(
        id: Int
    ): Organisation? = suspendTransaction(db) {
        getOrganisationByCondition { Organisations.id eq id }
    }
    
    suspend fun getOrganisationByName(
        name: String
    ): Organisation? = suspendTransaction(db) {
        getOrganisationByCondition { Organisations.name eq name }
    }
    
    suspend fun getOrganisationByExternalId(
        externalId: String
    ): Organisation? = suspendTransaction(db) {
        getOrganisationByCondition { Organisations.externalId eq externalId }
    }
    
    suspend fun doesOrgExist(extId: String) =
        suspendTransaction(db) { getOrganisationByExternalId(extId) != null }
    
    suspend fun insertOrg(
        org: Organisation
    ): Boolean = suspendTransaction(db) {
        if (!doesOrgExist(org.externalId))
            Organisations.insert {
            
            }.insertedCount == 1
        else false
    }
}