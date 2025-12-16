package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.*
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.models.*
import com.smokinggunstudio.vezerfonal.repositories.*
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
suspend fun UserData.toUser(
    mainDB: Database,
    db: Database? = null
): Pair<Database, User> {
    val db: Database = suspend getDB@{
        val code = registrationCode?.let {
            RegistrationCodeRepository(mainDB).getCodeByCode(it)
                ?: error("Registration code not found")
        } ?: return@getDB db!!
        ensureOrgDB(code.organisation.name)
            ?: error("Could not resolve database for organisation: ${code.organisation.name}")
    }()
    
    
    
    return Pair(
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

@OptIn(ExperimentalTime::class)
suspend fun MessageData.toMessage(db: Database): Message {
    val urepo = UserRepository(db)
    val grepo = GroupRepository(db)
    
    val author = urepo.getUserByIdentifier(author.identifier)!!
    val tagList = tags.map { tagName ->
        TagRepository(db)
            .getTagByName(tagName)
            ?: error("Tag is not available.")
    }
    
    var group: Group? = null
    var user: User? = null
    
    val users = userIdentifiers
        .orEmpty()
        .map { urepo.getUserByIdentifier(it)!! }
    val groupData = groups
        .orEmpty()
        .map { grepo.getGroupByExtId(it)!! }
    val groups = groupData.map {
        grepo.getExactGroupByNameAndAdminIdentifier(
            name = it.displayName,
            identifier = it.admin.identifier
        )!!
    }
    val allGroupUsers = groups
        .flatMap { group -> group.members.map { it.user } }
    val combinedUsers = users + allGroupUsers
    
    when(combinedUsers.size) {
        0 -> error("Both user and group cannot be null.")
        1 -> user = combinedUsers.single()
        else -> group = grepo.createInternalGroup(combinedUsers)
    }
    
    return Message(
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
        externalId = externalId,
        createdAt = null,
        updatedAt = null,
        deletedAt = null
    )
}

@OptIn(ExperimentalTime::class)
fun OrgData.toOrganisation() =
    Organisation(
        id = null,
        name = name,
        externalId = externalId,
        createdAt = Clock.System.now(),
    )

@OptIn(ExperimentalTime::class)
suspend fun GroupData.toGroup(
    db: Database
): Group {
    val urepo = UserRepository(db)
    val admin = urepo.getUserByIdentifier(adminIdentifier)!!
    val memberships = members.map { identifier ->
        Membership(
            user = urepo.getUserByIdentifier(identifier)!!,
            groupId = null,
            joinedAt = Clock.System.now()
        )
    }
    
    return Group(
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

fun RegCodeData.toRegCode(org: Organisation) =
    RegistrationCode(
        id = null,
        code = code,
        totalUses = totalUses,
        remainingUses = remainingUses,
        organisation = org
    )

suspend fun InteractionInfoData.toInteractionInfo(user: User, db: Database): InteractionInfo {
    val urepo = UserRepository(db)
    val mrepo = MessageRepository(db)
    
    val message = mrepo.getMessageByExtId(messageExtId)!!
    val recipient = recipientIdentifier
        ?.let { urepo.getUserByIdentifier(it) }
    
    return when (type) {
        InteractionType.status -> InteractionInfo(
            message = message,
            user = user,
            type = type,
            status = status,
        )
        InteractionType.reaction -> InteractionInfo(
            message = message,
            user = user,
            type = type,
            reaction = reaction!!
        )
        InteractionType.nudge -> InteractionInfo(
            message = message,
            user = user,
            type = type,
            recipient = recipient!!
        )
        InteractionType.archive -> InteractionInfo(
            message = message,
            user = user,
            type = type,
        )
    }
}