package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.helpers.toKotlinInstant
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.ExperimentalTime

class MessageRepository(val db: Database) {
    @OptIn(ExperimentalTime::class)
    private suspend fun ResultRow.toMessage(): Message =
        suspendTransaction(db) {
            val grepo = GroupRepository(db)
            val urepo = UserRepository(db)
            
            val user = this@toMessage[Messages.userId]?.let { urepo.getUserById(it) }
            val group = this@toMessage[Messages.groupId]?.let { grepo.getGroupById(it) }
            val author = urepo.getUserById(this@toMessage[Messages.authorUserId])!!
            val selectedTags = TagRepository(db).getTagsByMessageId(this@toMessage[Messages.id])
            
            if ((user == null) == (group == null))
                throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
            
            Message(
                id = this@toMessage[Messages.id],
                user = user,
                group = group,
                title = this@toMessage[Messages.title],
                content = this@toMessage[Messages.content],
                isUrgent = this@toMessage[Messages.isUrgent],
                author = author,
                availableReactions = this@toMessage[Messages.availableReactions],
                status = null,
                tags = selectedTags,
                externalId = this@toMessage[Messages.externalId],
                createdAt = this@toMessage[Messages.createdAt].toKotlinInstant(),
                updatedAt = this@toMessage[Messages.updatedAt].toKotlinInstant(),
                deletedAt = this@toMessage[Messages.deletedAt]?.toKotlinInstant()
            )
        }
    
    suspend fun getAllMessages(): List<Message> =
        suspendTransaction(db) {
            Messages
                .selectAll()
                .map { it.toMessage() }
        }
    
    suspend fun getMessageByCondition(
        condition: SQLCondition
    ): Message? = suspendTransaction(db) {
        Messages
            .select(condition)
            .toList()
            .ifNotEmpty()
            ?.single()
            ?.toMessage()
    }
    
    suspend fun getMessagesByCondition(
        limit: Int? = null,
        condition: SQLCondition
    ): List<Message> = suspendTransaction(db) {
        val messages =
            if (limit == null) Messages.select(condition)
            else Messages.select(condition).limit(limit)
        
        messages.map { it.toMessage() }
    }
    
    suspend fun getMessageById(
        id: Int,
    ): Message? = suspendTransaction(db) {
        getMessageByCondition { Messages.id eq id }
    }
    
    suspend fun getMessageByExtId(
        extId: String,
    ): Message? = suspendTransaction(db) {
        getMessageByCondition { Messages.externalId eq extId }
    }
    
    suspend fun getMessagesByUserId(
        id: Int,
    ): List<Message> = suspendTransaction(db) { getMessagesByCondition { Messages.userId eq id } }
    
    suspend fun getMessagesByUserIdentifier(
        identifier: String,
    ): List<Message> = suspendTransaction(db) {
        val user = UserRepository(db).getUserByExternalId(identifier)
            ?: return@suspendTransaction emptyList()
        
        getMessagesByCondition { Messages.userId eq user.id!! }
    }
    
    suspend fun getMessagesByGroupId(
        id: Int,
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition { Messages.groupId eq id }
    }
    
    suspend fun getMessagesBySenderUserId(
        id: Int,
        limit: Int? = null
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition(limit) { Messages.authorUserId eq id }
    }
    
    suspend fun getMessagesByRecipientUserId(
        id: Int,
        limit: Int? = null
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition(limit) {
            (Messages.userId eq id) or
                    (Messages.groupId inList UserGroupConnection
                        .select { UserGroupConnection.userId eq id }
                        .map { it[UserGroupConnection.groupId] })
        }
    }
    
    suspend fun getNonArchivedMessagesByRecipientUserId(
        id: Int,
        limit: Int? = null
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition(limit) {
            (Messages.userId eq id) or
            (Messages.groupId inList UserGroupConnection
                .select { UserGroupConnection.userId eq id }
                .map { it[UserGroupConnection.groupId] }) and
            (Messages.id notInSubQuery MessageUserInteractions
                .select(MessageUserInteractions.messageId)
                .where { MessageUserInteractions.type eq InteractionType.archive })
        }
    }
    
    suspend fun getArchivedMessagesByRecipientUserId(
        id: Int,
        limit: Int? = null
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition(limit) {
            ((Messages.userId eq id) or
            (Messages.groupId inList UserGroupConnection
                .select { UserGroupConnection.userId eq id }
                .map { it[UserGroupConnection.groupId] })) and
            (Messages.id inSubQuery MessageUserInteractions
                .select(MessageUserInteractions.messageId)
                .where { MessageUserInteractions.type eq InteractionType.archive })
        }
    }
    
    suspend fun getMessagesByTagId(
        id: Int,
    ): List<Message> = suspendTransaction(db) {
        getMessagesByCondition {
            (MessageTagConnection.tagId eq id) and
                    (Messages.id eq MessageTagConnection.messageId)
        }
    }
    
    suspend fun insertMessage(
        message: Message,
    ): Pair<Int, Boolean> = suspendTransaction(db) {
        val trepo = TagRepository(db)
        
        val statement = Messages.insert {
            it[userId] = message.user?.id
            it[groupId] = message.group?.id
            it[title] = message.title
            it[content] = message.content
            it[isUrgent] = message.isUrgent
            it[externalId] = message.externalId
            it[authorUserId] = message.author.id!!
            it[availableReactions] = message.availableReactions
        }
        
        val tagIds = message.tags.map { trepo.getTagByName(it.name)!!.id!! }
        trepo.attachTagsToMessageId(
            newMessageId = statement[Messages.id],
            tagIds = tagIds
        )
        
        Pair(statement[Messages.id], statement.insertedCount == 1)
    }
}