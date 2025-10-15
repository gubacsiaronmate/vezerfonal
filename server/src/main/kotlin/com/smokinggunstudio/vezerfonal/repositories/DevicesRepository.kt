package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Device
import com.smokinggunstudio.vezerfonal.objects.Devices
import kotlinx.coroutines.withContext
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
    condition: (Device) -> Boolean
): Device? = getAllDevices(context).firstOrNull(condition)

suspend fun getDevicesByCondition(
    context: CoroutineContext,
    condition: (Device) -> Boolean
): List<Device> = getAllDevices(context).filter(condition)