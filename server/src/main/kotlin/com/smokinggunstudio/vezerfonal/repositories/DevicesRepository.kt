package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Device
import com.smokinggunstudio.vezerfonal.objects.Devices
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllDevices(context: CoroutineContext): List<Device> = withContext(context) {
    val users = getAllUsers(context)
    val sessions = getAllSessions(context)
    return@withContext transaction { 
        val devices = Devices.selectAll()
        return@transaction devices.map { device ->
            val user = users.first { user -> user.id == device[Devices.userId] }
            val session = sessions.first { session -> session.id == device[Devices.sessionId] }
            Device(
                id = device[Devices.id],
                user = user,
                pushToken = device[Devices.pushToken],
                session = session,
                createdAt = device[Devices.createdAt],
                updatedAt = device[Devices.updatedAt],
                deletedAt = device[Devices.deletedAt]
            )
        }
    }
}

suspend fun getDeviceByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): Device? = withContext(context) {
    val users = getAllUsers(context)
    val sessions = getAllSessions(context)
    Devices.select(condition).firstOrNull()?.let { device ->
        val user = users.first { user -> user.id == device[Devices.userId] }
        val session = sessions.first { session -> session.id == device[Devices.sessionId] }
        Device(
            id = device[Devices.id],
            user = user,
            pushToken = device[Devices.pushToken],
            session = session,
            createdAt = device[Devices.createdAt],
            updatedAt = device[Devices.updatedAt],
            deletedAt = device[Devices.deletedAt]
        )
    }
}

suspend fun getDevicesByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): List<Device> = withContext(context) {
    val users = getAllUsers(context)
    val sessions = getAllSessions(context)
    Devices.select(condition).map { device ->
        val user = users.first { user -> user.id == device[Devices.userId] }
        val session = sessions.first { session -> session.id == device[Devices.sessionId] }
        Device(
            id = device[Devices.id],
            user = user,
            pushToken = device[Devices.pushToken],
            session = session,
            createdAt = device[Devices.createdAt],
            updatedAt = device[Devices.updatedAt],
            deletedAt = device[Devices.deletedAt]
        )
    }
}