package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Session
import com.smokinggunstudio.vezerfonal.objects.Sessions
import io.ktor.server.plugins.NotFoundException
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllSessions(context: CoroutineContext): List<Session> = withContext(context) {
    val users = getAllUsers(context)
    val jwts = getAllJWTs(context)
    transaction {
        val sessions = Sessions.selectAll()
        sessions.map { session ->
            val user = users.first { user -> user.id == session[Sessions.userId] }
            if (jwts.find { jwt -> jwt.id == session[Sessions.jti] } == null)
                throw NotFoundException("Session jwt not found")
            Session(
                id = session[Sessions.id],
                user = user,
                jti = session[Sessions.jti],
                refreshToken = session[Sessions.refreshTokenHash],
                ipAddress = session[Sessions.ipAddress],
                revoked = session[Sessions.revoked],
                deviceInfo = session[Sessions.deviceInfo],
                lastUsedAt = session[Sessions.lastUsedAt],
                expiresAt = session[Sessions.expiresAt],
                createdAt = session[Sessions.createdAt],
                updatedAt = session[Sessions.updatedAt],
                deletedAt = session[Sessions.deletedAt]
            )
        }
    }
}

suspend fun getSessionByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): Session? = withContext(context) {
    val users = getAllUsers(context)
    val jwts = getAllJWTs(context)
    Sessions.select(condition).firstOrNull()?.let { session ->
        val user = users.first { user -> user.id == session[Sessions.userId] }
        if (jwts.find { jwt -> jwt.id == session[Sessions.jti] } == null)
            throw NotFoundException("Session jwt not found")
        Session(
            id = session[Sessions.id],
            user = user,
            jti = session[Sessions.jti],
            refreshToken = session[Sessions.refreshTokenHash],
            ipAddress = session[Sessions.ipAddress],
            revoked = session[Sessions.revoked],
            deviceInfo = session[Sessions.deviceInfo],
            lastUsedAt = session[Sessions.lastUsedAt],
            expiresAt = session[Sessions.expiresAt],
            createdAt = session[Sessions.createdAt],
            updatedAt = session[Sessions.updatedAt],
            deletedAt = session[Sessions.deletedAt]
        )
    }
}

suspend fun getSessionsByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): List<Session> = withContext(context) {
    val users = getAllUsers(context)
    val jwts = getAllJWTs(context)
    Sessions.select(condition).map { session ->
        val user = users.first { user -> user.id == session[Sessions.userId] }
        if (jwts.find { jwt -> jwt.id == session[Sessions.jti] } == null)
            throw NotFoundException("Session jwt not found")
        Session(
            id = session[Sessions.id],
            user = user,
            jti = session[Sessions.jti],
            refreshToken = session[Sessions.refreshTokenHash],
            ipAddress = session[Sessions.ipAddress],
            revoked = session[Sessions.revoked],
            deviceInfo = session[Sessions.deviceInfo],
            lastUsedAt = session[Sessions.lastUsedAt],
            expiresAt = session[Sessions.expiresAt],
            createdAt = session[Sessions.createdAt],
            updatedAt = session[Sessions.updatedAt],
            deletedAt = session[Sessions.deletedAt]
        )
    }
}

suspend fun getSessionByUserId(
    id: Int,
    context: CoroutineContext
): Session? = newSuspendedTransaction { getSessionByCondition(context) { Sessions.userId eq id } }

suspend fun getSessionsByUserId(
    id: Int,
    context: CoroutineContext
): List<Session> = newSuspendedTransaction { getSessionsByCondition(context) { Sessions.userId eq id } }

