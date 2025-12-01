package com.smokinggunstudio.vezerfonal.database.triggers

import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.helpers.toSingle
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.MembershipRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

private suspend fun ResultRow.toGroup(db: Database): Group = suspendTransaction(db) {
    val admin = UserRepository(db).getUserById(this@toGroup[Groups.groupAdminId])!!
    val members = MembershipRepository(db).getMembershipsByGroupId(this@toGroup[Groups.id])
    
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

suspend fun trgAddToDefaultGroup(newUserId: Int, db: Database) = suspendTransaction(db) {
    val default = Groups
        .select { Groups.displayName eq "default" }
        .toSingle()
        ?.toGroup(db)
    
    suspend fun addToGroup(
        defaultGroupId: Int,
    ) = suspendTransaction(db) {
        UserGroupConnection.insert {
            it[groupId] = defaultGroupId
            it[userId] = newUserId
            it[joinedAt] = LocalDateTime.now()
        }
    }
    
    suspend fun getSuperAdminId(): Int = suspendTransaction(db) {
        Users.select { Users.isSuperAdmin eq true }.toSingle()!![Users.id]
    }
    
    suspend fun createDefaultGroup(): Int = suspendTransaction(db) {
        val adminId = getSuperAdminId()
        Groups.insert {
            it[displayName] = "default"
            it[description] = ""
            it[groupAdminId] = adminId
        }[Groups.id]
    }
    
    if (default != null) addToGroup(default.id!!)
    else {
        val groupId = createDefaultGroup()
        addToGroup(groupId)
    }
}