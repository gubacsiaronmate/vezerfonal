package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.createInternalGroup
import com.smokinggunstudio.vezerfonal.repositories.getCodeByCode
import com.smokinggunstudio.vezerfonal.repositories.getGroupByAdminIdentifier
import com.smokinggunstudio.vezerfonal.repositories.getTagByName
import com.smokinggunstudio.vezerfonal.repositories.getUserById
import com.smokinggunstudio.vezerfonal.repositories.getUserByIdentifier
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun UserData.toUser(context: CoroutineContext): User = withContext(context) {
    val code = getCodeByCode(registrationCode, context) ?: error("Code cannot be null!")
    
    User(
        registrationCode = code,
        email = email,
        displayName = name,
        identifier = identifier,
        isSuperAdmin = isSuperAdmin,
        
        id = null,
        profilePic = null,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    ).let { user ->
        user.password = password!!
        user
    }
}

suspend fun MessageData.toMessage(authorId: Int, context: CoroutineContext): Message = withContext(context) {
    val author = getUserById(authorId, context) ?: error("")
    val tagList = tags.map { tagName -> getTagByName(tagName, context) ?: error("Tag is not available.") }
    
    var group: Group? = null
    var user: User? = null
    
    val users = userIdentifiers.orEmpty().map { getUserByIdentifier(it, context)!! }
    val groups = groudAdminIdentifiers.orEmpty().map { getGroupByAdminIdentifier(it, context)!! }
    val allGroupUsers = groups.flatMap { group -> group.members.map { it.user } }
    val combinedUsers = users + allGroupUsers
    
    when(combinedUsers.size) {
        0 -> error("Both user and group cannot be null.")
        1 -> user = combinedUsers.single()
        else -> group = createInternalGroup(combinedUsers, context)
    }
    
    Message(
        id = null,
        user = user,
        group = group,
        title = title,
        content = content,
        isUrgent = isUrgent,
        author = author,
        availableReactions = availableReactions,
        status = status ?: MessageStatus.sent,
        tags = tagList,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}