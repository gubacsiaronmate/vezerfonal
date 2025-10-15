package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllInteractionInfo(context: CoroutineContext): List<InteractionInfo> = withContext(context) {
    val messages = getAllMessages(context)
    val users = getAllUsers(context)
    return@withContext transaction { 
        val infos = MessageUserInteractions.selectAll()
        
        return@transaction infos.map { info ->
            val message = messages.first { message -> message.id == info[MessageUserInteractions.messageId] }
            val user = users.first { user -> user.id == info[MessageUserInteractions.userId] }
            val actor = users.firstOrNull { user -> user.id == info[MessageUserInteractions.actorUserId] }
            
            InteractionInfo(
                id = info[MessageUserInteractions.id],
                message = message,
                user = user,
                type = info[MessageUserInteractions.type],
                status = info[MessageUserInteractions.status],
                reaction = info[MessageUserInteractions.reaction],
                actor = actor,
                createdAt = info[MessageUserInteractions.createdAt],
                updatedAt = info[MessageUserInteractions.updatedAt],
                deletedAt = info[MessageUserInteractions.deletedAt]
            )
        }
    }
}

suspend fun getInteractionInfoByCondition(
    context: CoroutineContext,
    condition: (InteractionInfo) -> Boolean
): InteractionInfo? = getAllInteractionInfo(context).firstOrNull(condition)

suspend fun getInteractionInfosByCondition(
    context: CoroutineContext,
    condition: (InteractionInfo) -> Boolean
): List<InteractionInfo> = getAllInteractionInfo(context).filter(condition)

suspend fun getInteractionInfoById(
    id: Int,
    context: CoroutineContext
): InteractionInfo? = getInteractionInfoByCondition(context) { info -> info.id == id }

suspend fun getInteractionInfosByMessageId(
    id: Int,
    context: CoroutineContext
): List<InteractionInfo> = getInteractionInfosByCondition(context) { info -> info.message.id == id }

suspend fun getInteractionInfosByUserId(
    id: Int,
    context: CoroutineContext
): List<InteractionInfo> = getInteractionInfosByCondition(context) { info -> info.user.id == id }