package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllMemberships(
    context: CoroutineContext
): List<Membership> = withContext(context) {
    UserGroupConnection.selectAll().map { membership ->
        val user = getUserById(membership[UserGroupConnection.userId], context)!!
        Membership(
            user = user,
            groupId = membership[UserGroupConnection.groupId],
            joinedAt = membership[UserGroupConnection.joinedAt]
        )
    }
}

suspend fun getMembershipByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): Membership? = withContext(context) {
    newSuspendedTransaction {
        UserGroupConnection.select(condition).firstOrNull()?.let { membership ->
            val user = getUserById(membership[UserGroupConnection.userId], context)!!
            Membership(
                user = user,
                groupId = membership[UserGroupConnection.groupId],
                joinedAt = membership[UserGroupConnection.joinedAt]
            )
        }
    }
}

suspend fun getMembershipsByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<Membership> = withContext(context) {
    newSuspendedTransaction {
        UserGroupConnection.select(condition).map { membership ->
            val user = getUserById(membership[UserGroupConnection.userId], context)!!
            Membership(
                user = user,
                groupId = membership[UserGroupConnection.groupId],
                joinedAt = membership[UserGroupConnection.joinedAt]
            )
        }
    }
}

suspend fun getMembershipsByGroupId(
    id: Int,
    context: CoroutineContext
): List<Membership> = newSuspendedTransaction {
    getMembershipsByCondition(context) { UserGroupConnection.groupId eq id }
}