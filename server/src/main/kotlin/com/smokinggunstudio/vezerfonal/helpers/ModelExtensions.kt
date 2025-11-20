package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.createInternalGroup
import com.smokinggunstudio.vezerfonal.repositories.getCodeByCode
import com.smokinggunstudio.vezerfonal.repositories.getGroupByAdminIdentifier
import com.smokinggunstudio.vezerfonal.repositories.getGroupById
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
    
    val areOnlyUsersAvailable =
        groudAdminIdentifiers.isNullOrEmpty()
            && !userIdentifiers.isNullOrEmpty()
    
    val areOnlyGroupsAvailable =
        !groudAdminIdentifiers.isNullOrEmpty()
                && userIdentifiers.isNullOrEmpty()
    
    val areBothAvailable =
        !groudAdminIdentifiers.isNullOrEmpty()
                && !userIdentifiers.isNullOrEmpty()
    
    when {
        areBothAvailable -> {
            group = createInternalGroup(
                members = listOf(
                    groudAdminIdentifiers!!.map { id ->
                        getGroupByAdminIdentifier(id, context)!!.members.map { (user, _) -> user }
                    }.flatten(),
                    userIdentifiers!!.map { getUserByIdentifier(it, context)!! }
                ).flatten(),
                context = context
            )
        }
        areOnlyUsersAvailable && userIdentifiers!!.size > 1 -> {
            group = createInternalGroup(
                userIdentifiers!!.map { getUserByIdentifier(it, context)!! },
                context
            )
        }
        areOnlyUsersAvailable && userIdentifiers!!.size == 1 -> {
            user = getUserByIdentifier(userIdentifiers!!.first(), context)
        }
        areOnlyGroupsAvailable && groudAdminIdentifiers!!.size == 1 -> {
            group = getGroupByAdminIdentifier(groudAdminIdentifiers!!.first(), context)
        }
        areOnlyGroupsAvailable && groudAdminIdentifiers!!.size > 1 -> {
            group = createInternalGroup(
                members = groudAdminIdentifiers!!.map { id ->
                    getGroupByAdminIdentifier(id, context)!!.members.map { m -> m.user }
                }.flatten(),
                context = context
            )
        }
    }
    
    if (user == null && group == null) error("Both user and group cannot be null.")
    
    Message(
        id = null,
        user = user,
        group = group,
        title = title,
        content = content,
        isUrgent = isUrgent,
        author = author,
        tags = tagList,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}