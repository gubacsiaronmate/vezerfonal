package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

suspend fun getAllGroups(context: CoroutineContext): List<Group> = withContext(context) {
    newSuspendedTransaction {
        Groups.selectAll().map { group ->
            val admin = getUserById(group[Groups.groupAdminId], context)!!
            val members = getMembershipsByGroupId(group[Groups.id], context)
            
            Group(
                id = group[Groups.id],
                displayName = group[Groups.displayName],
                description = group[Groups.description],
                members = members,
                admin = admin,
                isInternal = group[Groups.isInternal],
                createdAt = group[Groups.createdAt],
                updatedAt = group[Groups.updatedAt],
                deletedAt = group[Groups.deletedAt]
            )
        }
    }
}

suspend fun getGroupByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): Group? = withContext(context) {
    newSuspendedTransaction {
        Groups.select(condition).firstOrNull()?.let { group ->
            val admin = getUserById(group[Groups.groupAdminId], context)!!
            val members = getMembershipsByGroupId(group[Groups.id], context)
            
            Group(
                id = group[Groups.id],
                displayName = group[Groups.displayName],
                description = group[Groups.description],
                members = members,
                admin = admin,
                isInternal = group[Groups.isInternal],
                createdAt = group[Groups.createdAt],
                updatedAt = group[Groups.updatedAt],
                deletedAt = group[Groups.deletedAt]
            )
        }
    }
}

suspend fun getGroupsByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<Group> = withContext(context) {
    newSuspendedTransaction {
        Groups.select(condition).map { group ->
            val admin = getUserById(group[Groups.groupAdminId], context)!!
            val members = getMembershipsByGroupId(group[Groups.id], context)
            
            Group(
                id = group[Groups.id],
                displayName = group[Groups.displayName],
                description = group[Groups.description],
                members = members,
                admin = admin,
                isInternal = group[Groups.isInternal],
                createdAt = group[Groups.createdAt],
                updatedAt = group[Groups.updatedAt],
                deletedAt = group[Groups.deletedAt]
            )
        }
    }
}

suspend fun getGroupById(
    id: Int,
    context: CoroutineContext
): Group? = newSuspendedTransaction { getGroupByCondition(context) { Groups.id eq id } }

suspend fun getGroupByAdminId(
    id: Int,
    context: CoroutineContext
): Group? = newSuspendedTransaction { getGroupByCondition(context) { Groups.groupAdminId eq id } }

suspend fun getGroupsByAdminId(
    id: Int,
    context: CoroutineContext
): List<Group> = newSuspendedTransaction { getGroupsByCondition(context) { Groups.groupAdminId eq id } }

suspend fun getGroupByAdminIdentifier(
    identifier: String,
    context: CoroutineContext
): Group? = newSuspendedTransaction {
    getGroupByCondition(context) {
        Users.select { Groups.groupAdminId eq Users.id }.let { result ->
            val user = result.firstOrNull() ?: return@getGroupByCondition Op.FALSE
            if (user[Users.identifier] == identifier) Op.TRUE
            else Op.FALSE
        }
    }
}

suspend fun getAllGroupsByMemberUserId(
    id: Int,
    context: CoroutineContext,
): List<Group> = getAllGroups(context).filter { group -> group.members.any { membership -> membership.user.id == id } }

suspend fun getGroupByName(
    name: String,
    context: CoroutineContext
): Group? = newSuspendedTransaction { getGroupByCondition(context) { Groups.displayName eq name } }

suspend fun doesGroupExist(
    identifier: String,
    name: String,
    context: CoroutineContext
): Boolean = newSuspendedTransaction {
    val groupByAdmin = getGroupByAdminIdentifier(identifier, context)
    val groupByName = getGroupByName(name, context)
    
    groupByAdmin != null && (groupByAdmin == groupByName)
}

suspend fun insertGroup(
    group: Group,
    context: CoroutineContext
): Int = withContext(context) {
    val doesGroupExist = doesGroupExist(group.admin.identifier, group.displayName, context)
    val admin = getUserByIdentifier(group.admin.identifier, context)
    if (!doesGroupExist && admin != null) transaction {
        Groups.insert {
            it[displayName] = group.displayName
            it[description] = group.description
            it[groupAdminId] = admin.id!!
        }[Groups.id]
    } else -1
}

@OptIn(ExperimentalUuidApi::class)
suspend fun createInternalGroup(members: List<User>, context: CoroutineContext): Group =
    withContext(context) {
        newSuspendedTransaction {
            val group = Group(
                id = null,
                displayName = Uuid.random().toString(),
                description = "",
                members = members.map { user -> Membership(user, null, LocalDateTime.now()) },
                admin = createInternalUser(context),
                isInternal = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            )
            insertGroup(group, context)
            group
        }
    }