package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.coroutines.CoroutineContext

private suspend fun ResultRow.toMembership(): Membership =
    newSuspendedTransaction {
        val user = getUserById(this@toMembership[UserGroupConnection.userId])!!
        Membership(
            user = user,
            groupId = this@toMembership[UserGroupConnection.groupId],
            joinedAt = this@toMembership[UserGroupConnection.joinedAt]
        )
    }

suspend fun getAllMemberships(): List<Membership> =
    newSuspendedTransaction {
        UserGroupConnection
            .selectAll()
            .map { it.toMembership() }
    }

suspend fun getMembershipByCondition(
    condition: SQLCondition
): Membership? = newSuspendedTransaction {
    UserGroupConnection
        .select(condition)
        .toList()
        .ifNotEmpty()
        ?.single()
        ?.toMembership()
}

suspend fun getMembershipsByCondition(
    condition: SQLCondition
): List<Membership> = newSuspendedTransaction {
    UserGroupConnection
        .select(condition)
        .map { it.toMembership() }
}

suspend fun getMembershipsByGroupId(
    id: Int,
): List<Membership> = newSuspendedTransaction {
    getMembershipsByCondition { UserGroupConnection.groupId eq id }
}

suspend fun insertMemberIntoGroup(
    newUserId: Int,
    newGroupId: Int,
    newJoinedAt: LocalDateTime = LocalDateTime.now()
): Boolean = newSuspendedTransaction {
    UserGroupConnection.insert {
        it[userId] = newUserId
        it[groupId] = newGroupId
        it[joinedAt] = newJoinedAt
    }.insertedCount == 1
}