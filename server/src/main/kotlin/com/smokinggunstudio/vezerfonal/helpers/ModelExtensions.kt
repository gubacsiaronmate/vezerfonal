package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.RegistrationCodeRepository
import com.smokinggunstudio.vezerfonal.repositories.TagRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

suspend fun UserData.toUser(
    context: CoroutineContext,
    mainDB: Database,
    db: Database? = null
): Pair<Database, User> = withContext(context) {
    val db: Database = suspend getDB@{
        val code = registrationCode?.let {
            RegistrationCodeRepository(mainDB).getCodeByCode(it)
                ?: error("Registration code not found")
        } ?: return@getDB db!!
        ensureOrgDB(code.organisation.name, context)
            ?: error("Could not resolve database for organisation: ${code.organisation.name}")
    }()
    
    
    
    Pair(
        db,
        User(
            email = email,
            displayName = name,
            identifier = identifier,
            isAnyAdmin = isAnyAdmin,
            isSuperAdmin = isSuperAdmin,
            
            id = null,
            profilePic = null,
            createdAt = null,
            updatedAt = null,
            deletedAt = null
        ).apply { password = this@toUser.password!! }
    )
}

suspend fun MessageData.toMessage(
    authorId: Int,
    context: CoroutineContext,
    db: Database
): Message = withContext(context) {
    val urepo = UserRepository(db)
    val grepo = GroupRepository(db)
    
    val author = urepo.getUserById(authorId) ?: error("")
    val tagList = tags.map { tagName -> TagRepository(db).getTagByName(tagName) ?: error("Tag is not available.") }
    
    var group: Group? = null
    var user: User? = null
    
    val users = userIdentifiers.orEmpty().map { urepo.getUserByIdentifier(it)!! }
    val groups = groups.orEmpty().map { grepo.getExactGroupByNameAndAdminIdentifier(it.name, it.adminIdentifier)!! }
    val allGroupUsers = groups.flatMap { group -> group.members.map { it.user } }
    val combinedUsers = users + allGroupUsers
    
    when(combinedUsers.size) {
        0 -> error("Both user and group cannot be null.")
        1 -> user = combinedUsers.single()
        else -> group = grepo.createInternalGroup(combinedUsers)
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

fun OrgData.toOrganisation() =
    Organisation(
        id = null,
        name = name,
        externalId = externalId,
        createdAt = LocalDateTime.now()
    )

suspend fun GroupData.toGroup(
    context: CoroutineContext,
    db: Database
): Group = withContext(context) {
    val urepo = UserRepository(db)
    val admin = urepo.getUserByIdentifier(adminIdentifier)!!
    val memberships = members.map { identifier ->
        Membership(
            user = urepo.getUserByIdentifier(identifier)!!,
            groupId = null,
            joinedAt = LocalDateTime.now()
        )
    }
    
    Group(
        id = null,
        displayName = name,
        description = description,
        members = memberships,
        admin = admin,
        externalId = externalId,
        isInternal = false,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}