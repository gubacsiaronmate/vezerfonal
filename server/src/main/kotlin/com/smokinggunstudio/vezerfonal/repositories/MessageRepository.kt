package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.objects.Messages
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllMessages(context: CoroutineContext): List<Message> = withContext(context) {
    val users = getAllUsers(context)
    val groups = getAllGroups(context)
    return@withContext transaction { 
        val messages = Messages.selectAll()
        return@transaction messages.map { message ->
            val user = users.firstOrNull { user -> user.id == message[Messages.userId] }
            val group = groups.firstOrNull { group -> group.id == message[Messages.groupId] }
            val author = users.first { user -> user.id == message[Messages.authorUserId] }
            
            if ((user == null) == (group == null)) // one must be null at all times, but only one can be
                throw IllegalStateException("Both user and group cannot be null at the same time. Nor can both have a value.")
            
            Message(
                id = message[Messages.id],
                user = user,
                group = group,
                content = message[Messages.content],
                isUrgent = message[Messages.isUrgent],
                author = author,
                createdAt = message[Messages.createdAt],
                updatedAt = message[Messages.updatedAt],
                deletedAt = message[Messages.deletedAt]
            )
        }
    }
}

suspend fun getMessageByCondition(
    context: CoroutineContext,
    condition: (Message) -> Boolean
): Message? = getAllMessages(context).firstOrNull(condition)

suspend fun getMessagesByCondition(
    context: CoroutineContext,
    condition: (Message) -> Boolean
): List<Message> = getAllMessages(context).filter(condition)

suspend fun getMessageById(
    id: Int,
    context: CoroutineContext
): Message? = getMessageByCondition(context) { message -> message.id == id }

suspend fun getMessageByUserId(
    id: Int,
    context: CoroutineContext
): Message? = getMessageByCondition(context) { message -> message.user?.id == id }

suspend fun getMessageByUserIdentifier(
    identifier: String,
    context: CoroutineContext
): Message? = getMessageByCondition(context) { message -> message.user?.identifier == identifier }

suspend fun getMessagesByGroupId(
    id: Int,
    context: CoroutineContext
): List<Message> = getMessagesByCondition(context) { message -> message.group?.id == id }

suspend fun getMessagesByRecipientUserId(
    id: Int,
    context: CoroutineContext
): List<Message> = getMessagesByCondition(context) { message -> message.group?.members?.any { (user, _) -> user.id == id } == true }