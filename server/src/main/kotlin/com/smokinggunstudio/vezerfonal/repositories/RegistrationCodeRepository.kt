package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.RegistrationCode
import com.smokinggunstudio.vezerfonal.objects.RegistrationCodes
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update

class RegistrationCodeRepository(val db: Database) {
    private suspend fun ResultRow.toCode(): RegistrationCode = suspendTransaction(db) {
        val organisation = OrganisationRepository(db)
            .getOrganisationById(this@toCode[RegistrationCodes.organisationId])!!
        
        RegistrationCode(
            id = this@toCode[RegistrationCodes.id],
            code = this@toCode[RegistrationCodes.code],
            totalUses = this@toCode[RegistrationCodes.totalUses],
            remainingUses = this@toCode[RegistrationCodes.remainingUses],
            organisation = organisation
        )
    }
    
    suspend fun getAllCodes(): List<RegistrationCode> =
        suspendTransaction(db) {
            RegistrationCodes.selectAll().map { it.toCode() }
        }
    
    suspend fun getCodeByCondition(
        condition: SQLCondition
    ): RegistrationCode? = suspendTransaction(db) {
        RegistrationCodes.select(condition).toList().ifNotEmpty()?.single()?.toCode()
    }
    
    suspend fun getCodesByCondition(
        condition: SQLCondition
    ): List<RegistrationCode> = suspendTransaction(db) {
        RegistrationCodes.select(condition).map { it.toCode() }
    }
    
    suspend fun getCodeById(
        id: Int,
    ): RegistrationCode? = suspendTransaction(db) {
        getCodeByCondition { RegistrationCodes.id eq id }
    }
    
    suspend fun getCodeByCode(
        code: String,
    ): RegistrationCode? = suspendTransaction(db) {
        getCodeByCondition { RegistrationCodes.code eq code }
    }
    
    suspend fun doesCodeExist(
        code: String,
    ): Boolean = suspendTransaction(db) { getCodeByCode(code) != null }
    
    suspend fun insertCode(
        registrationCode: RegistrationCode,
    ): Boolean = suspendTransaction(db) {
        val org = OrganisationRepository(db)
            .getOrganisationByExternalId(
                registrationCode.organisation.externalId
            )!!
        
        if (!doesCodeExist(registrationCode.code))
            RegistrationCodes.insert {
                it[code] = registrationCode.code
                it[totalUses] = registrationCode.totalUses
                it[remainingUses] = registrationCode.remainingUses
                it[organisationId] = org.id!!
            }.insertedCount == 1
        else false
    }
    
    suspend fun insertCodes(
        registrationCodes: List<RegistrationCode>,
    ): List<Boolean> = suspendTransaction(db) {
        registrationCodes.map { insertCode(it) }
    }
    
    suspend fun updateCode(
        registrationCode: RegistrationCode,
    ): Boolean = suspendTransaction(db) {
        RegistrationCodes.update({ RegistrationCodes.code eq registrationCode.code }) {
            it[totalUses] = registrationCode.totalUses
            it[remainingUses] = registrationCode.remainingUses
        } == 1
    }
    
    suspend fun deleteCode(
        code: String
    ): Boolean = suspendTransaction(db) {
        RegistrationCodes.deleteWhere {
            RegistrationCodes.code eq code
        } == 1
    }
}