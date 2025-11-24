package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import com.smokinggunstudio.vezerfonal.objects.MessageUserInteractions
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import com.smokinggunstudio.vezerfonal.objects.Users
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun toMessage(
    context: CoroutineContext,
    message: ResultRow
): Message {
    val user = message[Messages.userId]?.let { getUserById(it, context) }
    val group = message[Messages.groupId]?.let { getGroupById(it, context) }
    val author = getUserById(message[Messages.authorUserId], context)!!
    val selectedTags = getTagsByMessageId(message[Messages.id], context)
    val status = getInteractionInfosByCondition(context) {
        (MessageUserInteractions.messageId eq message[Messages.id]) and
                (MessageUserInteractions.type eq InteractionType.status)
    }.maxByOrNull { info -> info.createdAt!! }?.let { it.status!! } ?: MessageStatus.sent
    
    if ((user == null) == (group == null))
        throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
    
    return Message(
        id = message[Messages.id],
        user = user,
        group = group,
        title = message[Messages.title],
        content = message[Messages.content],
        isUrgent = message[Messages.isUrgent],
        author = author,
        availableReactions = message[Messages.availableReactions],
        status = status,
        tags = selectedTags,
        createdAt = message[Messages.createdAt],
        updatedAt = message[Messages.updatedAt],
        deletedAt = message[Messages.deletedAt]
    )
}

suspend fun Query.toMessageList(
    context: CoroutineContext
): List<Message> = map { message -> toMessage(context, message) }

suspend fun getAllMessages(
    context: CoroutineContext
): List<Message> = newSuspendedTransaction { Messages.selectAll().toMessageList(context) }

suspend fun getMessageByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): Message? = withContext(context) {
    Messages
        .select(condition)
        .firstOrNull()
        ?.let { message ->
            toMessage(context, message)
        }
}

suspend fun getMessagesByCondition(
    context: CoroutineContext,
    limit: Int? = null,
    condition: SQLCondition
): List<Message> = withContext(context) {
    val messages = if (limit == null)
        Messages.select(condition)
    else
        Messages.select(condition).limit(limit)
    messages.toMessageList(context)
}

suspend fun getMessageById(
    id: Int,
    context: CoroutineContext
): Message? = newSuspendedTransaction { getMessageByCondition(context) { Messages.id eq id } }

suspend fun getMessageByUserId(
    id: Int,
    context: CoroutineContext
): Message? = newSuspendedTransaction { getMessageByCondition(context) { Messages.userId eq id } }

suspend fun getMessageByUserIdentifier(
    identifier: String,
    context: CoroutineContext
): Message? = newSuspendedTransaction {
    getMessageByCondition(context) {
        Messages.userId eq Users.select {
            Users.identifier eq identifier
        }.first()[Users.id]
    }
}

suspend fun getMessagesByGroupId(
    id: Int,
    context: CoroutineContext
): List<Message> = getMessagesByCondition(context) { Messages.groupId eq id }

suspend fun getMessagesByUserId(
    id: Int,
    context: CoroutineContext,
    limit: Int? = null
): List<Message> = newSuspendedTransaction {
    getMessagesByCondition(context, limit) {
        val ugc = UserGroupConnection.alias("ugc")
        
        (Messages.userId eq id) or exists(
            ugc.select {
                (ugc[UserGroupConnection.groupId] eq Messages.groupId) and
                (ugc[UserGroupConnection.userId] eq id)
            }
        )
    }
}

suspend fun getMessagesByRecipientUserId(
    id: Int,
    context: CoroutineContext
): List<Message> = getAllMessages(context).filter { message ->
    message.group?.members?.any { membership ->
        membership.user.id == id
    } == true
}

suspend fun getMessagesByTagId(
    id: Int,
    context: CoroutineContext
): List<Message> = newSuspendedTransaction {
    getMessagesByCondition(context) {
        (MessageTagConnection.tagId eq id) and
        (Messages.id eq MessageTagConnection.messageId)
    }
}

suspend fun insertMessage(
    message: Message,
    context: CoroutineContext,
): Boolean = withContext(context) {
    transaction {
        Messages.insert {
            it[userId] = message.user?.id
            it[groupId] = message.group?.id
            it[title] = message.title
            it[content] = message.content
            it[isUrgent] = message.isUrgent
            it[authorUserId] = message.author.id!!
        }.insertedCount == 1
    }
}