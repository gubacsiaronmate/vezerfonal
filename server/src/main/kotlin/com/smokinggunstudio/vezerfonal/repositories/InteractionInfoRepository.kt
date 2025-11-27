package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

private suspend fun ResultRow.toInteractionInfo(): InteractionInfo =
    suspendTransaction {
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
    suspendTransaction {
        MessageUserInteractions
            .selectAll()
            .map { it.toInteractionInfo() }
    }

suspend fun getInteractionInfoByCondition(
    condition: SQLCondition
): InteractionInfo? = suspendTransaction {
    MessageUserInteractions
        .select(condition)
        .toList()
        .ifNotEmpty()
        ?.single()
        ?.toInteractionInfo()
}

suspend fun getInteractionInfosByCondition(
    condition: SQLCondition
): List<InteractionInfo> = suspendTransaction {
    MessageUserInteractions
        .select(condition)
        .map { it.toInteractionInfo() }
}

suspend fun getInteractionInfoById(
    id: Int,
): InteractionInfo? = suspendTransaction {
    getInteractionInfoByCondition { MessageUserInteractions.id eq id }
}

suspend fun getInteractionInfosByMessageId(
    id: Int,
): List<InteractionInfo> = suspendTransaction {
    getInteractionInfosByCondition { MessageUserInteractions.messageId eq id }
}

suspend fun getInteractionInfosByUserId(
    id: Int,
): List<InteractionInfo> = suspendTransaction {
    getInteractionInfosByCondition { MessageUserInteractions.userId eq id }
}

suspend fun insertInteraction(
    interaction: InteractionInfo,
): Boolean = suspendTransaction {
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