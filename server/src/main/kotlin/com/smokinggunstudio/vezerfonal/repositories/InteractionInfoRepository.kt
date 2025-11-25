package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

private suspend fun ResultRow.toInteractionInfo(): InteractionInfo =
    newSuspendedTransaction {
        val message = getMessageById(this@toInteractionInfo[MessageUserInteractions.messageId])!!
        val user = getUserById(this@toInteractionInfo[MessageUserInteractions.userId])!!
        val actor = this@toInteractionInfo[MessageUserInteractions.actorUserId]?.let { getUserById(it) }
        
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

suspend fun getAllInteractionInfo(): List<InteractionInfo> =
    newSuspendedTransaction {
        MessageUserInteractions
            .selectAll()
            .map { it.toInteractionInfo() }
    }

suspend fun getInteractionInfoByCondition(
    condition: SQLCondition
): InteractionInfo? = newSuspendedTransaction {
    MessageUserInteractions
        .select(condition)
        .toList()
        .ifNotEmpty()
        ?.single()
        ?.toInteractionInfo()
}

suspend fun getInteractionInfosByCondition(
    condition: SQLCondition
): List<InteractionInfo> = newSuspendedTransaction {
    MessageUserInteractions
        .select(condition)
        .map { it.toInteractionInfo() }
}

suspend fun getInteractionInfoById(
    id: Int,
): InteractionInfo? = newSuspendedTransaction {
    getInteractionInfoByCondition { MessageUserInteractions.id eq id }
}

suspend fun getInteractionInfosByMessageId(
    id: Int,
): List<InteractionInfo> = newSuspendedTransaction {
    getInteractionInfosByCondition { MessageUserInteractions.messageId eq id }
}

suspend fun getInteractionInfosByUserId(
    id: Int,
): List<InteractionInfo> = newSuspendedTransaction {
    getInteractionInfosByCondition { MessageUserInteractions.userId eq id }
}

suspend fun insertInteraction(
    interaction: InteractionInfo,
): Boolean = newSuspendedTransaction {
    val actorUserID = interaction.actor?.identifier?.let { getUserByIdentifier(it)?.id }
    MessageUserInteractions.insert {
        it[messageId] = interaction.message.id!!
        it[userId] = interaction.user.id!!
        it[type] = interaction.type
        it[status] = interaction.status
        it[reaction] = interaction.reaction
        it[actorUserId] = actorUserID
    }.insertedCount == 1
}