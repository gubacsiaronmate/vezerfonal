package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllInteractionInfo(context: CoroutineContext): List<InteractionInfo> = withContext(context) {
    newSuspendedTransaction {
        MessageUserInteractions.selectAll().map { info ->
            val message = getMessageById(info[MessageUserInteractions.messageId], context)!!
            val user = getUserById(info[MessageUserInteractions.userId], context)!!
            val actor = info[MessageUserInteractions.actorUserId]?.let { getUserById(it, context) }
            
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
    condition: SQLCondition
): InteractionInfo? = withContext(context) {
    MessageUserInteractions.select(condition).firstOrNull()?.let { info ->
        val message = getMessageById(info[MessageUserInteractions.messageId], context)!!
        val user = getUserById(info[MessageUserInteractions.userId], context)!!
        val actor = info[MessageUserInteractions.actorUserId]?.let { getUserById(it, context) }
        
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

suspend fun getInteractionInfosByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<InteractionInfo> = withContext(context) {
    MessageUserInteractions.select(condition).map { info ->
        val message = getMessageById(info[MessageUserInteractions.messageId], context)!!
        val user = getUserById(info[MessageUserInteractions.userId], context)!!
        val actor = info[MessageUserInteractions.actorUserId]?.let { getUserById(it, context) }
        
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

suspend fun getInteractionInfoById(
    id: Int,
    context: CoroutineContext
): InteractionInfo? = newSuspendedTransaction {
    getInteractionInfoByCondition(context) { MessageUserInteractions.id eq id }
}

suspend fun getInteractionInfosByMessageId(
    id: Int,
    context: CoroutineContext
): List<InteractionInfo> = newSuspendedTransaction {
    getInteractionInfosByCondition(context) { MessageUserInteractions.messageId eq id }
}

suspend fun getInteractionInfosByUserId(
    id: Int,
    context: CoroutineContext
): List<InteractionInfo> = newSuspendedTransaction {
    getInteractionInfosByCondition(context) { MessageUserInteractions.userId eq id }
}

suspend fun insertInteraction(
    interaction: InteractionInfo,
    context: CoroutineContext
): Boolean = withContext(context) {
    transaction {
        MessageUserInteractions.insert {
            it[messageId] = interaction.message.id!!
            it[userId] = interaction.user.id!!
            it[type] = interaction.type
            it[status] = interaction.status
            it[reaction] = interaction.reaction
            it[actorUserId] = interaction.actor?.id!!
        }.insertedCount == 1
    }
}