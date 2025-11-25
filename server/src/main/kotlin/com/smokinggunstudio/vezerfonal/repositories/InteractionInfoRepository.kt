package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

private suspend fun ResultRow.toInteractionInfo(
    context: CoroutineContext
): InteractionInfo = newSuspendedTransaction {
    val message = getMessageById(this@toInteractionInfo[MessageUserInteractions.messageId], context)!!
    val user = getUserById(this@toInteractionInfo[MessageUserInteractions.userId], context)!!
    val actor = this@toInteractionInfo[MessageUserInteractions.actorUserId]?.let { getUserById(it, context) }
    
    InteractionInfo(
        id = this@toInteractionInfo[MessageUserInteractions.id],
        message = message,
        user = user,
        type = this@toInteractionInfo[MessageUserInteractions.type],
        status = this@toInteractionInfo[MessageUserInteractions.status],
        reaction = this@toInteractionInfo[MessageUserInteractions.reaction],
        actor = actor,
        createdAt = this@toInteractionInfo[MessageUserInteractions.createdAt],
        updatedAt = this@toInteractionInfo[MessageUserInteractions.updatedAt],
        deletedAt = this@toInteractionInfo[MessageUserInteractions.deletedAt]
    )
}

suspend fun getAllInteractionInfo(
    context: CoroutineContext
): List<InteractionInfo> = newSuspendedTransaction {
    MessageUserInteractions.selectAll().map { it.toInteractionInfo(context) }
}

suspend fun getInteractionInfoByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): InteractionInfo? = newSuspendedTransaction {
    MessageUserInteractions
        .select(condition)
        .firstOrNull()
        ?.toInteractionInfo(context)
}

suspend fun getInteractionInfosByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<InteractionInfo> = newSuspendedTransaction {
    MessageUserInteractions
        .select(condition)
        .map { it.toInteractionInfo(context) }
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