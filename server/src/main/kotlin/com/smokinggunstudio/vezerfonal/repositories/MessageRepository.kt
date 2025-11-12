package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.objects.Messages
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllMessages(context: CoroutineContext): List<Message> = withContext(context) {
    val users = getAllUsers(context)
    val groups = getAllGroups(context)
    val tags = getAllTags(context)
    return@withContext transaction { 
        val messages = Messages.selectAll()
        return@transaction messages.map { message ->
            val user = users.firstOrNull { user -> user.id == message[Messages.userId] }
            val group = groups.firstOrNull { group -> group.id == message[Messages.groupId] }
            val author = users.first { user -> user.id == message[Messages.authorUserId] }
            val selectedTags = tags.filter { tag -> tag.messageIds.any { it == message[Messages.id] } }
            
            if ((user == null) == (group == null)) // one must be null at all times, but only one can be
                throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
            
            Message(
                id = message[Messages.id],
                user = user,
                group = group,
                title = message[Messages.title],
                content = message[Messages.content],
                isUrgent = message[Messages.isUrgent],
                author = author,
                tags = selectedTags,
                createdAt = message[Messages.createdAt],
                updatedAt = message[Messages.updatedAt],
                deletedAt = message[Messages.deletedAt]
            )
        }
    }
}

suspend fun getMessageByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): Message? = withContext(context) {
    val users = getAllUsers(context)
    val groups = getAllGroups(context)
    val tags = getAllTags(context)
    Messages.select(condition).firstOrNull()?.let { message ->
        val user = users.firstOrNull { user -> user.id == message[Messages.userId] }
        val group = groups.firstOrNull { group -> group.id == message[Messages.groupId] }
        val author = users.first { user -> user.id == message[Messages.authorUserId] }
        val selectedTags = tags.filter { tag -> tag.messageIds.any { it == message[Messages.id] } }
        if ((user == null) == (group == null))
            throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
        Message(
            id = message[Messages.id],
            user = user,
            group = group,
            title = message[Messages.title],
            content = message[Messages.content],
            isUrgent = message[Messages.isUrgent],
            author = author,
            tags = selectedTags,
            createdAt = message[Messages.createdAt],
            updatedAt = message[Messages.updatedAt],
            deletedAt = message[Messages.deletedAt]
        )
    }
}

suspend fun getMessagesByCondition(
    context: CoroutineContext,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): List<Message> = withContext(context) {
    val users = getAllUsers(context)
    val groups = getAllGroups(context)
    val tags = getAllTags(context)
    Messages.select(condition).map { message ->
        val user = users.firstOrNull { user -> user.id == message[Messages.userId] }
        val group = groups.firstOrNull { group -> group.id == message[Messages.groupId] }
        val author = users.first { user -> user.id == message[Messages.authorUserId] }
        val selectedTags = tags.filter { tag -> tag.messageIds.any { it == message[Messages.id] } }
        if ((user == null) == (group == null))
            throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
        Message(
            id = message[Messages.id],
            user = user,
            group = group,
            title = message[Messages.title],
            content = message[Messages.content],
            isUrgent = message[Messages.isUrgent],
            author = author,
            tags = selectedTags,
            createdAt = message[Messages.createdAt],
            updatedAt = message[Messages.updatedAt],
            deletedAt = message[Messages.deletedAt]
        )
    }
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
): Message? = getAllMessages(context).firstOrNull { message -> message.user?.identifier == identifier }

suspend fun getMessagesByGroupId(
    id: Int,
    context: CoroutineContext
): List<Message> = getMessagesByCondition(context) { Messages.groupId eq id }

suspend fun getMessagesByUserId(
    id: Int,
    context: CoroutineContext
): List<Message> = getMessagesByCondition(context) { Messages.userId eq id  }

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
): List<Message> = getAllMessages(context).filter { message -> message.tags.any { tag -> tag.id == id } }