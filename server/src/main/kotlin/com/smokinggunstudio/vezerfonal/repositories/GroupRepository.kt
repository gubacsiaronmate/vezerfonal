package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.Membership
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Groups
import com.smokinggunstudio.vezerfonal.objects.Messages
import com.smokinggunstudio.vezerfonal.objects.UserGroupConnection
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GroupRepository(val db: Database) {
    private suspend fun ResultRow.toGroup(): Group = suspendTransaction(db) {
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
    
    suspend fun getAllGroups(): List<Group> =
        suspendTransaction(db) {
            Groups.selectAll().map { it.toGroup() }
        }
    
    suspend fun getGroupByCondition(
        condition: SQLCondition
    ): Group? = suspendTransaction(db) {
        Groups
            .select(condition)
            .toList()
            .ifNotEmpty()
            ?.single()
            ?.toGroup()
    }
    
    suspend fun getGroupsByCondition(
        condition: SQLCondition
    ): List<Group> = suspendTransaction(db) {
        Groups.select(condition).map { it.toGroup() }
    }
    
    suspend fun getGroupById(
        id: Int,
    ): Group? = suspendTransaction(db) { getGroupByCondition { Groups.id eq id } }
    
    suspend fun getGroupsByAdminId(
        id: Int,
    ): List<Group> = suspendTransaction(db) { getGroupsByCondition { Groups.groupAdminId eq id } }
    
    
    suspend fun getExactGroupByNameAndAdminIdentifier(
        name: String,
        identifier: String,
    ): Group? = suspendTransaction(db) {
        val admin = UserRepository(db).getUserByIdentifier(identifier)
            ?: return@suspendTransaction null
        
        getGroupByCondition {
            (Groups.displayName eq name) and
                    (Groups.groupAdminId eq admin.id!!)
        }
    }
    
    suspend fun getAllGroupsByMemberUserId(
        id: Int,
    ): List<Group> = suspendTransaction(db) {
        getGroupsByCondition {
            Groups.id inSubQuery UserGroupConnection
                .select { UserGroupConnection.userId eq id }
                .adjustSelect { select(UserGroupConnection.groupId) }
        }
    }
    
    suspend fun getGroupByName(
        name: String,
    ): Group? = suspendTransaction(db) { getGroupByCondition { Groups.displayName eq name } }
    
    suspend fun getGroupsByMessageId(
        id: Int,
    ): List<Group> = suspendTransaction(db) {
        getGroupsByCondition { (Messages.id eq id) and (Messages.groupId eq Groups.id) }
    }
    
    suspend fun doesGroupExist(
        name: String,
        identifier: String,
    ): Boolean = suspendTransaction(db) {
        getExactGroupByNameAndAdminIdentifier(name, identifier) != null
    }
    
    suspend fun insertGroup(
        group: Group,
    ): Boolean = suspendTransaction(db) {
        val doesGroupExist = doesGroupExist(
            name = group.displayName,
            identifier = group.admin.identifier
        )
        val admin = UserRepository(db).getUserByIdentifier(group.admin.identifier)
        if (!doesGroupExist && admin != null) {
            val insert = Groups.insert {
                it[displayName] = group.displayName
                it[description] = group.description
                it[groupAdminId] = admin.id!!
            }
            group.members.forEach {
                MembershipRepository(db).insertMemberIntoGroup(
                    newUserId = UserRepository(db).getUserByIdentifier(it.user.identifier)!!.id!!,
                    newGroupId = insert[Groups.id],
                )
            }
            insert.insertedCount == 1
        }
        else false
    }
    
    @OptIn(ExperimentalUuidApi::class)
    suspend fun createInternalGroup(
        members: List<User>,
    ): Group = suspendTransaction(db) {
        val groupName = Uuid.random().toString()
        val admin = UserRepository(db).createInternalUser()
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
        memberships.forEach {
            MembershipRepository(db).insertMemberIntoGroup(
                newUserId = UserRepository(db).getUserByIdentifier(it.user.identifier)!!.id!!,
                newGroupId = group.id!!,
            )
        }
        group
    }
}