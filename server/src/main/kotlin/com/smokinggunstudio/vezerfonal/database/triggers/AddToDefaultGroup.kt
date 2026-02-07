package com.smokinggunstudio.vezerfonal.database.triggers

import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.MembershipRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
private suspend fun ResultRow.toGroup(db: Database): Group = suspendTransaction(db) {
    val admin = UserRepository(db).getUserById(this@toGroup[Groups.groupAdminId])!!
    val members = MembershipRepository(db).getMembershipsByGroupId(this@toGroup[Groups.id])
    
    Group(
        id = this@toGroup[Groups.id],
        displayName = this@toGroup[Groups.displayName],
        description = this@toGroup[Groups.description],
        members = members,
        admin = admin,
        externalId = this@toGroup[Groups.externalId],
        isInternal = this@toGroup[Groups.isInternal],
        createdAt = this@toGroup[Groups.createdAt].toKotlinInstant(),
        updatedAt = this@toGroup[Groups.updatedAt].toKotlinInstant(),
        deletedAt = this@toGroup[Groups.deletedAt]?.toKotlinInstant()
    )
}

@OptIn(ExperimentalTime::class)
suspend fun trgAddToDefaultGroup(newUserId: Int, db: Database) = suspendTransaction(db) {
    val default = Groups
        .select { Groups.displayName eq "default" }
        .singleOrNull()
        ?.toGroup(db)
    
    suspend fun addToGroup(
        defaultGroupId: Int,
    ) = suspendTransaction(db) {
        UserGroupConnection.insert {
            it[groupId] = defaultGroupId
            it[userId] = newUserId
            it[joinedAt] = Clock.System.now().toOffsetDateTime(ZoneOffset.UTC)
        }
    }
    
    suspend fun getSuperAdminId(): Int = suspendTransaction(db) {
        Users.select { Users.isSuperAdmin eq true }.singleOrNull()?.get(Users.id) ?: 1
    }
    
    suspend fun createDefaultGroup(): Int = suspendTransaction(db) {
        val adminId = getSuperAdminId()
        Groups.insert {
            it[displayName] = "default"
            it[description] = ""
            it[groupAdminId] = adminId
            it[externalId] = getExtId()
        }[Groups.id]
    }
    
    if (default != null) addToGroup(default.id!!)
    else {
        val groupId = createDefaultGroup()
        addToGroup(groupId)
    }
}