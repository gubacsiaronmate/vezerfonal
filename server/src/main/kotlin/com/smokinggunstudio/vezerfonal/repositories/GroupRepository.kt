package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private suspend fun ResultRow.toGroup(): Group = newSuspendedTransaction {
    val admin = getUserById(this@toGroup[Groups.groupAdminId])!!
    val members = getMembershipsByGroupId(this@toGroup[Groups.id])
    
    Group(
        id = this@toGroup[Groups.id],
        displayName = this@toGroup[Groups.displayName],
        description = this@toGroup[Groups.description],
        members = members,
        admin = admin,
        isInternal = this@toGroup[Groups.isInternal],
        createdAt = this@toGroup[Groups.createdAt],
        updatedAt = this@toGroup[Groups.updatedAt],
        deletedAt = this@toGroup[Groups.deletedAt]
    )
}

suspend fun getAllGroups(): List<Group> =
    newSuspendedTransaction {
        Groups.selectAll().map { it.toGroup() }
    }

suspend fun getGroupByCondition(
    condition: SQLCondition
): Group? = newSuspendedTransaction {
    Groups
        .select(condition)
        .toList()
        .ifNotEmpty()
        ?.single()
        ?.toGroup()
}

suspend fun getGroupsByCondition(
    condition: SQLCondition
): List<Group> = newSuspendedTransaction {
    Groups.select(condition).map { it.toGroup() }
}

suspend fun getGroupById(
    id: Int,
): Group? = newSuspendedTransaction { getGroupByCondition { Groups.id eq id } }

suspend fun getGroupsByAdminId(
    id: Int,
): List<Group> = newSuspendedTransaction { getGroupsByCondition { Groups.groupAdminId eq id } }


suspend fun getExactGroupByNameAndAdminIdentifier(
    name: String,
    identifier: String,
): Group? = newSuspendedTransaction {
    val admin = getUserByIdentifier(identifier)
        ?: return@newSuspendedTransaction null
    
    getGroupByCondition {
        (Groups.displayName eq name) and
        (Groups.groupAdminId eq admin.id!!)
    }
}

suspend fun getAllGroupsByMemberUserId(
    id: Int,
): List<Group> = newSuspendedTransaction {
    getGroupsByCondition {
        Groups.id inSubQuery UserGroupConnection
            .select { UserGroupConnection.userId eq id }
            .adjustSelect { select(UserGroupConnection.groupId) }
    }
}

suspend fun getGroupByName(
    name: String,
): Group? = newSuspendedTransaction { getGroupByCondition { Groups.displayName eq name } }

suspend fun getGroupsByMessageId(
    id: Int,
): List<Group> = newSuspendedTransaction {
    getGroupsByCondition { (Messages.id eq id) and (Messages.groupId eq Groups.id) }
}

suspend fun doesGroupExist(
    name: String,
    identifier: String,
): Boolean = newSuspendedTransaction {
    getExactGroupByNameAndAdminIdentifier(name, identifier) != null
}

suspend fun insertGroup(
    group: Group,
): Boolean = newSuspendedTransaction {
    val doesGroupExist = doesGroupExist(
        name = group.displayName,
        identifier = group.admin.identifier
    )
    val admin = getUserByIdentifier(group.admin.identifier)
    if (!doesGroupExist && admin != null)
        Groups.insert {
            it[displayName] = group.displayName
            it[description] = group.description
            it[groupAdminId] = admin.id!!
        }.insertedCount == 1
    else false
}

@OptIn(ExperimentalUuidApi::class)
suspend fun createInternalGroup(
    members: List<User>,
): Group = newSuspendedTransaction {
    val groupName = Uuid.random().toString()
    val admin = createInternalUser()
    val memberships = members.map { user -> Membership(user, null, LocalDateTime.now()) }
    insertGroup(
        Group(
            id = null,
            displayName = groupName,
            description = "",
            members = memberships,
            admin = admin,
            isInternal = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )
    )
    val group = getExactGroupByNameAndAdminIdentifier(
        name = groupName,
        identifier = admin.identifier
    )!!
    memberships.forEach { insertMemberIntoGroup(
        newUserId = getUserByIdentifier(it.user.identifier)!!.id!!,
        newGroupId = group.id!!,
    ) }
    group
}