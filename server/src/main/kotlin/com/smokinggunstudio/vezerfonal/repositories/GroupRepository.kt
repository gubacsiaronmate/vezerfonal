package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllGroups(context: CoroutineContext): List<Group> = withContext(context) {
    val users = getAllUsers(context)
    val memberships = getAllMemberShips(context)
    return@withContext transaction {
        val groups = Groups.selectAll()
        return@transaction groups.map { group ->
            val admin = users.first { user -> user.id == group[Groups.groupAdminId] }
            val members = memberships
                .filter { it[UserGroupConnection.groupId] == group[Groups.id] }
                .map { memberships -> Membership(
                    user = users.first { user -> user.id == memberships[UserGroupConnection.userId] },
                    joinedAt = memberships[UserGroupConnection.joinedAt]
                ) }
            Group(
                id = group[Groups.id],
                displayName = group[Groups.displayName],
                description = group[Groups.description],
                members = members,
                admin = admin,
                createdAt = group[Groups.createdAt],
                updatedAt = group[Groups.updatedAt],
                deletedAt = group[Groups.deletedAt]
            )
        }
    }
}

suspend fun getGroupByCondition(
    context: CoroutineContext,
    condition: (Group) -> Boolean
): Group? = getAllGroups(context).firstOrNull(condition)

suspend fun getGroupsByCondition(
    context: CoroutineContext,
    condition: (Group) -> Boolean
): List<Group> = getAllGroups(context).filter(condition)

suspend fun getGroupById(
    id: Int,
    context: CoroutineContext
): Group? = getGroupByCondition(context) { group -> group.id == id }

suspend fun getGroupByAdminId(
    id: Int,
    context: CoroutineContext
): Group? = getGroupByCondition(context) { group -> group.admin.id == id }

suspend fun getGroupByAdminIdentifier(
    identifier: String,
    context: CoroutineContext
): Group? = getGroupByCondition(context) { group -> group.admin.identifier == identifier }

suspend fun getAllGroupsByMemberUserId(
    id: Int,
    context: CoroutineContext,
): List<Group> = getGroupsByCondition(context) { group -> group.members.any { membership -> membership.user.id == id } }