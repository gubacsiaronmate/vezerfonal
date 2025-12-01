package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class MembershipRepository(val db: Database) {
    private suspend fun ResultRow.toMembership(): Membership =
        suspendTransaction(db) {
            val user = UserRepository(db).getUserById(this@toMembership[UserGroupConnection.userId])!!
            Membership(
                user = user,
                groupId = this@toMembership[UserGroupConnection.groupId],
                joinedAt = this@toMembership[UserGroupConnection.joinedAt]
            )
        }
    
    suspend fun getAllMemberships(): List<Membership> =
        suspendTransaction(db) {
            UserGroupConnection
                .selectAll()
                .map { it.toMembership() }
        }
    
    suspend fun getMembershipByCondition(
        condition: SQLCondition
    ): Membership? = suspendTransaction(db) {
        UserGroupConnection
            .select(condition)
            .toList()
            .ifNotEmpty()
            ?.single()
            ?.toMembership()
    }
    
    suspend fun getMembershipsByCondition(
        condition: SQLCondition
    ): List<Membership> = suspendTransaction(db) {
        UserGroupConnection
            .select(condition)
            .map { it.toMembership() }
    }
    
    suspend fun getMembershipsByGroupId(
        id: Int,
    ): List<Membership> = suspendTransaction(db) {
        getMembershipsByCondition { UserGroupConnection.groupId eq id }
    }
    
    suspend fun insertMemberIntoGroup(
        newUserId: Int,
        newGroupId: Int,
        newJoinedAt: LocalDateTime = LocalDateTime.now()
    ): Boolean = suspendTransaction(db) {
        UserGroupConnection.insert {
            it[userId] = newUserId
            it[groupId] = newGroupId
            it[joinedAt] = newJoinedAt
        }.insertedCount == 1
    }
}