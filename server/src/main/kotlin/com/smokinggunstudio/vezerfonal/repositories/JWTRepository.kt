package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.objects.JWTs
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllJWTs(context: CoroutineContext): List<JWTModel> = withContext(context) {
    val users = getAllUsers(context)
    transaction {
        val jwts = JWTs.selectAll()
        jwts.map { jwt ->
            val user = users.first { user -> user.id == jwt[JWTs.userId] }
            JWTModel(
                id = jwt[JWTs.id],
                tokenHash = jwt[JWTs.tokenHash],
                isRefresh = jwt[JWTs.isRefresh],
                user = user,
                revoked = jwt[JWTs.revoked],
                createdAt = jwt[JWTs.createdAt],
                expiresAt = jwt[JWTs.expiresAt]
            )
        }
    }
}

suspend fun getJWTByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): JWTModel? = withContext(context) {
    val users = getAllUsers(context)
    JWTs.select(condition).firstOrNull()?.let { jwt ->
        JWTModel(
            id = jwt[JWTs.id],
            tokenHash = jwt[JWTs.tokenHash],
            isRefresh = jwt[JWTs.isRefresh],
            user = jwt[JWTs.userId].let { id -> users.first { user -> user.id == id } },
            revoked = jwt[JWTs.revoked],
            createdAt = jwt[JWTs.createdAt],
            expiresAt = jwt[JWTs.expiresAt]
        )
    }
}

suspend fun getJWTsByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<JWTModel> = withContext(context) {
    val users = getAllUsers(context)
    JWTs.select(condition).map { jwt ->
        JWTModel(
            id = jwt[JWTs.id],
            tokenHash = jwt[JWTs.tokenHash],
            isRefresh = jwt[JWTs.isRefresh],
            user = jwt[JWTs.userId].let { id -> users.first { user -> user.id == id } },
            revoked = jwt[JWTs.revoked],
            createdAt = jwt[JWTs.createdAt],
            expiresAt = jwt[JWTs.expiresAt]
        )
    }
}

suspend fun getJWTById(
    id: String,
    context: CoroutineContext
): JWTModel? = newSuspendedTransaction { getJWTByCondition(context) { JWTs.id eq id } }

suspend fun getJWTsByUserId(
    id: Int,
    context: CoroutineContext
): List<JWTModel> = newSuspendedTransaction { getJWTsByCondition(context) { JWTs.userId eq id } }

suspend fun getActiveJWTsByUserId(
    id: Int,
    context: CoroutineContext
): List<JWTModel> = newSuspendedTransaction {
    getJWTsByCondition(context) {
        (JWTs.userId eq id) and (JWTs.revoked eq false)
    }
}

suspend fun insertJWT(
    context: CoroutineContext,
    jwt: JWTModel
): Boolean = withContext(context) {
    if (getJWTById(jwt.id, context) == null) transaction {
        JWTs.insert { row ->
            row[id] = jwt.id
            row[tokenHash] = jwt.tokenHash
            row[isRefresh] = jwt.isRefresh
            row[userId] = jwt.user.id!!
            row[revoked] = jwt.revoked
            jwt.createdAt?.let { row[createdAt] = it }
            row[expiresAt] = jwt.expiresAt
        }.insertedCount == 1
    } else false
}

suspend fun <T> modifyJWT(
    tokenId: String,
    property: Column<T>,
    newValue: T,
    context: CoroutineContext
): Boolean = withContext(context) {
    val jwt = getJWTById(tokenId, context)
    if (jwt != null) transaction {
        JWTs.update({ JWTs.id eq jwt.id }) {
            it[property] = newValue
        } == 1
    } else false
}