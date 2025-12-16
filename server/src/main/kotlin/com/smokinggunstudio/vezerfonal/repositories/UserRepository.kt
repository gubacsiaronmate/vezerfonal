package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.database.triggers.trgAddToDefaultGroup
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Users
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.ZoneOffset
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepository(val db: Database) {
    @OptIn(ExperimentalTime::class)
    private suspend fun ResultRow.toUser(): User = suspendTransaction(db) {
        val pfp = this@toUser[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
        User(
            id = this@toUser[Users.id],
            email = this@toUser[Users.email],
            _password = this@toUser[Users.password],
            profilePic = pfp,
            displayName = this@toUser[Users.displayName],
            identifier = this@toUser[Users.identifier],
            isAnyAdmin = null,
            isSuperAdmin = this@toUser[Users.isSuperAdmin],
            createdAt = this@toUser[Users.createdAt].toKotlinInstant(),
            updatedAt = this@toUser[Users.updatedAt].toKotlinInstant(),
            deletedAt = this@toUser[Users.deletedAt]?.toKotlinInstant()
        )
    }
    
    suspend fun getAllUsers(): List<User> =
        suspendTransaction(db) {
            Users.selectAll().map { it.toUser() }
        }
    
    suspend fun getUserByCondition(
        condition: SQLCondition
    ): User? = suspendTransaction(db) {
        Users.select(condition).toList().ifNotEmpty()?.single()?.toUser()
    }
    
    suspend fun getUsersByCondition(
        condition: SQLCondition
    ): List<User> = suspendTransaction(db) {
        Users.select(condition).map { it.toUser() }
    }
    
    suspend fun getUserById(
        id: Int,
    ): User? = suspendTransaction(db) { getUserByCondition { Users.id eq id } }
    
    suspend fun getUserByEmail(
        email: String,
    ): User? = suspendTransaction(db) { getUserByCondition { Users.email eq email } }
    
    suspend fun getUserByIdentifier(
        identifier: String,
    ): User? = suspendTransaction(db) { getUserByCondition { Users.identifier eq identifier } }
    
    suspend fun doesUserExist(
        identifier: String
    ): Boolean = suspendTransaction(db) { getUserByIdentifier(identifier) != null }
    
    suspend fun insertUser(
        user: User,
    ): Boolean = suspendTransaction(db) {
        if (!doesUserExist(user.identifier)) {
            val insert = Users.insert {
                it[email] = user.email
                it[password] = user.password
                it[profilePicURI] = user.profilePic?.uri
                it[displayName] = user.displayName
                it[identifier] = user.identifier
                it[isSuperAdmin] = user.isSuperAdmin
            }
            val user = getUserById(insert[Users.id])!!
            if (!user.isSuperAdmin) trgAddToDefaultGroup(user.id!!, db)
            insert.insertedCount == 1
        } else false
    }
    
    @OptIn(ExperimentalTime::class)
    suspend fun <T> modifyUser(
        userId: Int,
        property: Column<T>,
        newValue: T,
    ): Boolean = suspendTransaction(db) {
        val user = getUserById(userId)
        if (user != null)
            Users.update({ Users.id eq user.id!! }) {
                it[property] = newValue
                it[updatedAt] = Clock.System.now().toOffsetDateTime(ZoneOffset.UTC)
            } == 1
        else false
    }
    
    @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
    suspend fun createInternalUser(): User =
        suspendTransaction(db) {
            val identifier = Uuid.random().toString().substring(0..8)
            insertUser(
                User(
                    id = null,
                    email = "${Uuid.random().toString().substring(0..8)}@example.com",
                    _password = Uuid.random().toString().substring(0..8),
                    profilePic = null,
                    displayName = Uuid.random().toString().substring(0..8),
                    identifier = identifier,
                    isAnyAdmin = true,
                    isSuperAdmin = false,
                    createdAt = null,
                    updatedAt = null,
                    deletedAt = null
                )
            )
            val user = getUserByIdentifier(identifier)!!
            user
        }
}