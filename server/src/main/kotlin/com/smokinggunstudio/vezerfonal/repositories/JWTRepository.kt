package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.objects.JWTs
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllJWTs(context: CoroutineContext): List<JWTModel> = withContext(context) {
    transaction {
        val jwts = JWTs.selectAll()
        jwts.map { jwt ->
            JWTModel(
                id = jwt[JWTs.id],
                tokenHash = jwt[JWTs.tokenHash],
                isRefresh = jwt[JWTs.isRefresh],
                revoked = jwt[JWTs.revoked],
                createdAt = jwt[JWTs.createdAt],
                expiresAt = jwt[JWTs.expiresAt]
            )
        }
    }
}

suspend fun getJWTByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): JWTModel? = withContext(context) {
    JWTs.select(condition).firstOrNull()?.let { jwt ->
        JWTModel(
            id = jwt[JWTs.id],
            tokenHash = jwt[JWTs.tokenHash],
            isRefresh = jwt[JWTs.isRefresh],
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

suspend fun insertJWT(
    context: CoroutineContext,
    jwt: JWTModel
): Boolean = withContext(context) {
    if (getJWTById(jwt.id, context) == null) transaction {
        JWTs.insert { row ->
            row[id] = jwt.id
            row[tokenHash] = jwt.tokenHash
            row[isRefresh] = jwt.isRefresh
            row[revoked] = jwt.revoked
            jwt.createdAt?.let { row[createdAt] = it }
            row[expiresAt] = jwt.expiresAt
        }.insertedCount == 1
    } else false
}