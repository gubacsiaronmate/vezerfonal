package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.ProfileImage
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Users
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private suspend fun ResultRow.toUser(): User = newSuspendedTransaction {
    val pfp = this@toUser[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
    val regCode = getCodeById(this@toUser[Users.registrationCodeId])!!
    User(
        id = this@toUser[Users.id],
        registrationCode = regCode,
        email = this@toUser[Users.email],
        _password = this@toUser[Users.password],
        profilePic = pfp,
        displayName = this@toUser[Users.displayName],
        identifier = this@toUser[Users.identifier],
        isAnyAdmin = null,
        isSuperAdmin = this@toUser[Users.isSuperAdmin],
        createdAt = this@toUser[Users.createdAt],
        updatedAt = this@toUser[Users.updatedAt],
        deletedAt = this@toUser[Users.deletedAt]
    )
}

suspend fun getAllUsers(): List<User> =
    newSuspendedTransaction {
        Users.selectAll().map { it.toUser() }
    }

suspend fun getUserByCondition(
    condition: SQLCondition
): User? = newSuspendedTransaction {
    Users.select(condition).toList().ifNotEmpty()?.single()?.toUser()
}

suspend fun getUsersByCondition(
    condition: SQLCondition
): List<User> = newSuspendedTransaction {
    Users.select(condition).map { it.toUser() }
}

suspend fun getUserById(
    id: Int,
): User? = newSuspendedTransaction { getUserByCondition { Users.id eq id } }

suspend fun getUserByEmail(
    email: String,
): User? = newSuspendedTransaction { getUserByCondition { Users.email eq email } }

suspend fun getUserByIdentifier(
    identifier: String,
): User? = newSuspendedTransaction { getUserByCondition { Users.identifier eq identifier } }

suspend fun doesUserExist(
    identifier: String
): Boolean = newSuspendedTransaction { getUserByIdentifier(identifier) != null }

suspend fun insertUser(
    user: User,
): Boolean = newSuspendedTransaction {
    val code = getCodeByCode(user.registrationCode.code)
        ?: return@newSuspendedTransaction false
    
    if (!doesUserExist(user.identifier))
        Users.insert {
            it[registrationCodeId] = code.id!!
            it[email] = user.email
            it[password] = user.password
            it[profilePicURI] = user.profilePic?.uri
            it[displayName] = user.displayName
            it[identifier] = user.identifier
            it[isSuperAdmin] = user.isSuperAdmin
        }.insertedCount == 1
    else false
}

suspend fun <T> modifyUser(
    userId: Int,
    property: Column<T>,
    newValue: T,
): Boolean = newSuspendedTransaction {
    val user = getUserById(userId)
    if (user != null) transaction {
        Users.update({ Users.id eq user.id!! }) {
            it[property] = newValue
        } == 1
    } else false
}

@OptIn(ExperimentalUuidApi::class)
suspend fun createInternalUser(): User =
    newSuspendedTransaction {
        val identifier = Uuid.random().toString().substring(0..8)
        insertUser(
            User(
                id = null,
                registrationCode = getCodeByCode("996633")!!,
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