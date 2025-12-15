package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.helpers.singleOrNull
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.objects.JWTs
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update

class JWTRepository(val db: Database) {
    private suspend fun ResultRow.toJWTModel(): JWTModel =
        suspendTransaction(db) {
            val user = UserRepository(db).getUserById(this@toJWTModel[JWTs.userId])!!
            JWTModel(
                id = this@toJWTModel[JWTs.id],
                tokenHash = this@toJWTModel[JWTs.tokenHash],
                isRefresh = this@toJWTModel[JWTs.isRefresh],
                user = user,
                revoked = this@toJWTModel[JWTs.revoked],
                createdAt = this@toJWTModel[JWTs.createdAt],
                expiresAt = this@toJWTModel[JWTs.expiresAt]
            )
        }
    
    suspend fun getAllJWTs(): List<JWTModel> =
        suspendTransaction(db) {
            JWTs.selectAll().map { it.toJWTModel() }
        }
    
    suspend fun getJWTByCondition(
        condition: SQLCondition
    ): JWTModel? = suspendTransaction(db) {
        JWTs
            .select(condition)
            .singleOrNull()
            ?.toJWTModel()
    }
    
    suspend fun getJWTsByCondition(
        condition: SQLCondition
    ): List<JWTModel> = suspendTransaction(db) {
        JWTs.select(condition).map { it.toJWTModel() }
    }
    
    suspend fun getJWTById(
        id: String,
    ): JWTModel? = suspendTransaction(db) { getJWTByCondition { JWTs.id eq id } }
    
    suspend fun getJWTByTokenHash(
        tokenHash: String
    ): JWTModel? = suspendTransaction(db) { getJWTByCondition { JWTs.tokenHash eq tokenHash } }
    
    suspend fun getJWTsByUserId(
        id: Int,
    ): List<JWTModel> = suspendTransaction(db) { getJWTsByCondition { JWTs.userId eq id } }
    
    suspend fun getActiveJWTsByUserId(
        id: Int,
    ): List<JWTModel> = suspendTransaction(db) {
        getJWTsByCondition { (JWTs.userId eq id) and (JWTs.revoked eq false) }
    }
    
    suspend fun doesJWTExist(
        tokenHash: String
    ): Boolean = suspendTransaction(db) { getJWTByTokenHash(tokenHash) != null }
    
    suspend fun insertJWT(
        jwt: JWTModel
    ): Boolean = suspendTransaction(db) {
        if (!doesJWTExist(jwt.tokenHash))
            JWTs.insert { row ->
                row[id] = jwt.id
                row[tokenHash] = jwt.tokenHash
                row[isRefresh] = jwt.isRefresh
                row[userId] = jwt.user.id!!
                row[revoked] = jwt.revoked
                jwt.createdAt?.let { row[createdAt] = it }
                row[expiresAt] = jwt.expiresAt
            }.insertedCount == 1
        else false
    }
    
    suspend fun <T> modifyJWT(
        tokenId: String,
        property: Column<T>,
        newValue: T,
    ): Boolean = suspendTransaction(db) {
        val jwt = getJWTById(tokenId)
        if (jwt != null)
            JWTs.update({ JWTs.id eq jwt.id }) {
                it[property] = newValue
            } == 1
        else false
    }
    
    suspend fun invalidateToken(
        tokenId: String
    ): Boolean = suspendTransaction(db) {
        modifyJWT(
            tokenId = tokenId,
            property = JWTs.revoked,
            newValue = true
        )
    }
    
    suspend fun invalidateAllTokensByUserId(
        userId: Int
    ): Boolean = suspendTransaction(db) {
        JWTs.update({ JWTs.userId eq userId }) {
            it[revoked] = true
        } > 0
    }
    
    suspend fun invalidateAllTokens(): Boolean =
        suspendTransaction(db) {
            JWTs.update {
                it[revoked] = true
            } > 0
        }
}