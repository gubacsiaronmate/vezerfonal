package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Session
import com.smokinggunstudio.vezerfonal.objects.Sessions
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllSessions(context: CoroutineContext): List<Session> = withContext(context) {
    val users = getAllUsers(context)
    return@withContext transaction {
        val sessions = Sessions.selectAll()
        return@transaction sessions.map { session ->
            val user = users.first { user -> user.id == session[Sessions.userId] }
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
    condition: (Session) -> Boolean
): Session? = getAllSessions(context).firstOrNull(condition)

suspend fun getSessionsByCondition(
    context: CoroutineContext,
    condition: (Session) -> Boolean
): List<Session> = getAllSessions(context).filter(condition)

suspend fun getSessionByUserId(
    id: Int,
    context: CoroutineContext
): Session? = getSessionByCondition(context) { session -> session.user.id == id }

suspend fun getSessionsByUserId(
    id: Int,
    context: CoroutineContext
): List<Session> = getSessionsByCondition(context) { session -> session.user.id == id }

