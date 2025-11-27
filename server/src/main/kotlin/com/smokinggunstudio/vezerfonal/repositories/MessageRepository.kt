package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

private suspend fun ResultRow.toMessage(): Message =
    suspendTransaction {
        val user = this@toMessage[Messages.userId]?.let { getUserById(it) }
        val group = this@toMessage[Messages.groupId]?.let { getGroupById(it) }
        val author = getUserById(this@toMessage[Messages.authorUserId])!!
        val selectedTags = getTagsByMessageId(this@toMessage[Messages.id])
        
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
            createdAt = this@toMessage[Messages.createdAt],
            updatedAt = this@toMessage[Messages.updatedAt],
            deletedAt = this@toMessage[Messages.deletedAt]
        )
    }

suspend fun getAllMessages(): List<Message> =
    suspendTransaction {
        Messages
            .selectAll()
            .map { it.toMessage() }
    }

suspend fun getMessageByCondition(
    condition: SQLCondition
): Message? = suspendTransaction {
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
): List<Message> = suspendTransaction {
    val messages =
        if (limit == null) Messages.select(condition)
        else Messages.select(condition).limit(limit)
    
    messages.map { it.toMessage() }
}

suspend fun getMessageById(
    id: Int,
): Message? = suspendTransaction { getMessageByCondition { Messages.id eq id } }

suspend fun getMessagesByUserId(
    id: Int,
): List<Message> = suspendTransaction { getMessagesByCondition { Messages.userId eq id } }

suspend fun getMessagesByUserIdentifier(
    identifier: String,
): List<Message> = suspendTransaction {
    val user = getUserByIdentifier(identifier)
        ?: return@suspendTransaction emptyList()
    
    getMessagesByCondition {
        Messages.userId eq user.id!!
    }
}

suspend fun getMessagesByGroupId(
    id: Int,
): List<Message> = suspendTransaction {
    getMessagesByCondition { Messages.groupId eq id }
}

suspend fun getMessagesBySenderUserId(
    id: Int,
    limit: Int? = null
): List<Message> = suspendTransaction {
    getMessagesByCondition(limit) { Messages.authorUserId eq id }
}

suspend fun getMessagesByRecipientUserId(
    id: Int,
): List<Message> = suspendTransaction {
    getMessagesByCondition {
        (Messages.userId eq id) or
        (Messages.groupId inList UserGroupConnection
            .select { UserGroupConnection.userId eq id }
            .map { it[UserGroupConnection.groupId] })
    }
}

suspend fun getMessagesByTagId(
    id: Int,
): List<Message> = suspendTransaction {
    getMessagesByCondition {
        (MessageTagConnection.tagId eq id) and
        (Messages.id eq MessageTagConnection.messageId)
    }
}

suspend fun insertMessage(
    message: Message,
): Boolean = suspendTransaction {
    val statement = Messages.insert {
        it[userId] = message.user?.id
        it[groupId] = message.group?.id
        it[title] = message.title
        it[content] = message.content
        it[isUrgent] = message.isUrgent
        it[authorUserId] = message.author.id!!
        it[availableReactions] = message.availableReactions
    }
    
    val tagIds = message.tags.map { getTagByName(it.tagName)!!.id!! }
    attachTagsToMessageId(
        newMessageId = statement[Messages.id],
        tagIds = tagIds
    )
    
    statement.insertedCount == 1
}