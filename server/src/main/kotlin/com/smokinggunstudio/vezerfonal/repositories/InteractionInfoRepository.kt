package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.helpers.toKotlinInstant
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.ExperimentalTime

class InteractionInfoRepository(val db: Database) {
    @OptIn(ExperimentalTime::class)
    private suspend fun ResultRow.toInteractionInfo(): InteractionInfo =
        suspendTransaction(db) {
            val urepo = UserRepository(db)
            
            val message = MessageRepository(db).getMessageById(this@toInteractionInfo[MessageUserInteractions.messageId])!!
            val user = urepo.getUserById(this@toInteractionInfo[MessageUserInteractions.userId])!!
            val recipient = this@toInteractionInfo[MessageUserInteractions.recipientUserId]?.let { urepo.getUserById(it) }
            
            InteractionInfo(
                id = this@toInteractionInfo[MessageUserInteractions.id],
                message = message,
                user = user,
                type = this@toInteractionInfo[MessageUserInteractions.type],
                status = this@toInteractionInfo[MessageUserInteractions.status],
                reaction = this@toInteractionInfo[MessageUserInteractions.reaction],
                recipient = recipient,
                createdAt = this@toInteractionInfo[MessageUserInteractions.createdAt].toKotlinInstant(),
                updatedAt = this@toInteractionInfo[MessageUserInteractions.updatedAt].toKotlinInstant(),
                deletedAt = this@toInteractionInfo[MessageUserInteractions.deletedAt]?.toKotlinInstant()
            )
        }
    
    suspend fun getAllInteractionInfo(): List<InteractionInfo> =
        suspendTransaction(db) {
            MessageUserInteractions
                .selectAll()
                .map { it.toInteractionInfo() }
        }
    
    suspend fun getInteractionInfoByCondition(
        condition: SQLCondition
    ): InteractionInfo? = suspendTransaction(db) {
        MessageUserInteractions
            .select(condition)
            .toList()
            .ifNotEmpty()
            ?.single()
            ?.toInteractionInfo()
    }
    
    suspend fun getInteractionInfosByCondition(
        condition: SQLCondition
    ): List<InteractionInfo> = suspendTransaction(db) {
        MessageUserInteractions
            .select(condition)
            .map { it.toInteractionInfo() }
    }
    
    suspend fun getInteractionInfoById(
        id: Int,
    ): InteractionInfo? = suspendTransaction(db) {
        getInteractionInfoByCondition { MessageUserInteractions.id eq id }
    }
    
    suspend fun getInteractionInfosByMessageId(
        id: Int,
    ): List<InteractionInfo> = suspendTransaction(db) {
        getInteractionInfosByCondition { MessageUserInteractions.messageId eq id }
    }
    
    suspend fun getInteractionInfosByMessageIdAndType(
        id: Int,
        type: InteractionType,
    ): List<InteractionInfo> = suspendTransaction(db) {
        getInteractionInfosByCondition {
            (MessageUserInteractions.messageId eq id) and
            (MessageUserInteractions.type eq type)
        }
    }

    suspend fun getInteractionInfosByMessageAndUserId(
        messageId: Int,
        userId: Int,
    ): List<InteractionInfo> = suspendTransaction(db) {
        getInteractionInfosByCondition {
            (MessageUserInteractions.messageId eq messageId) and
            (MessageUserInteractions.userId eq userId)
        }
    }
    
    suspend fun getInteractionInfosByUserId(
        id: Int,
    ): List<InteractionInfo> = suspendTransaction(db) {
        getInteractionInfosByCondition { MessageUserInteractions.userId eq id }
    }
    
    suspend fun insertInteraction(
        interaction: InteractionInfo,
    ): Boolean = suspendTransaction(db) {
        val actorUserID = interaction.recipient?.externalId?.let { UserRepository(db).getUserByExternalId(it)?.id }
        MessageUserInteractions.insert {
            it[messageId] = interaction.message.id!!
            it[userId] = interaction.user.id!!
            it[type] = interaction.type
            it[status] = interaction.status
            it[reaction] = interaction.reaction
            it[recipientUserId] = actorUserID
        }.insertedCount == 1
    }
}