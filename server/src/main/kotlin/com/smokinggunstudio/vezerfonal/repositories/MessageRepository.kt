package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

fun Query.toMessageList(
    users: List<User>,
    groups: List<Group>,
    tags: List<Tag>
): List<Message> = map { message ->
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

suspend fun getAllMessages(context: CoroutineContext): List<Message> = withContext(context) {
    val users: List<User> = getAllUsers(context)
    val groups: List<Group> = getAllGroups(context)
    val tags: List<Tag> = getAllTags(context)
    transaction {
        val messages = Messages.selectAll()
        messages.toMessageList(
            users,
            groups,
            tags
        )
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
    limit: Int? = null,
    condition: SqlExpressionBuilder.() -> Op<Boolean>
): List<Message> = withContext(context) {
    val users = getAllUsers(context)
    val groups = getAllGroups(context)
    val tags = getAllTags(context)
    val messages = if (limit == null)
        Messages.select(condition)
    else
        Messages.select(condition).limit(limit)
    messages.toMessageList(
        users,
        groups,
        tags
    )
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
): List<Message> = getAllMessages(context).filter { message -> message.tags.any { tag -> tag.id == id } }