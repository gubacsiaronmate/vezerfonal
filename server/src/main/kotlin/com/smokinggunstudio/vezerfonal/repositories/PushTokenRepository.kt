package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.PushToken
import com.smokinggunstudio.vezerfonal.objects.PushTokens
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class PushTokenRepository(val db: Database) {
    suspend fun registerToken(
        userId: Int,
        token: String,
        platform: String
    ): Boolean = suspendTransaction(db) {
        PushTokens.deleteWhere { (PushTokens.userId eq userId) and (PushTokens.token eq token) }
        
        PushTokens.insert {
            it[PushTokens.userId] = userId
            it[PushTokens.token] = token
            it[PushTokens.platform] = platform
        }.insertedCount == 1
    }
    
    suspend fun getTokensForUser(
        userId: Int
    ): List<String> = suspendTransaction(db) {
        PushTokens
            .select { PushTokens.userId eq userId }
            .map { it[PushTokens.token] }
    }
}