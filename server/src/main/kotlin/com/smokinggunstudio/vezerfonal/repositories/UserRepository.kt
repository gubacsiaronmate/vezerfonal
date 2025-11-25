package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.ProfileImage
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
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
    context: CoroutineContext,
    condition: SQLCondition
): User? = withContext(context) {
    newSuspendedTransaction {
        Users.select(condition).firstOrNull()?.let { user ->
            val pfp = user[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
            val regCode = getCodeById(user[Users.registrationCodeId])!!
            User(
                id = user[Users.id],
                registrationCode = regCode,
                email = user[Users.email],
                _password = user[Users.password],
                profilePic = pfp,
                displayName = user[Users.displayName],
                identifier = user[Users.identifier],
                isAnyAdmin = null,
                isSuperAdmin = user[Users.isSuperAdmin],
                createdAt = user[Users.createdAt],
                updatedAt = user[Users.updatedAt],
                deletedAt = user[Users.deletedAt]
            )
        }
    }
}

suspend fun getUsersByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<User> = withContext(context) {
    newSuspendedTransaction {
        Users.select(condition).map { user ->
            val pfp = user[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
            val regCode = getCodeById(user[Users.registrationCodeId])!!
            User(
                id = user[Users.id],
                registrationCode = regCode,
                email = user[Users.email],
                _password = user[Users.password],
                profilePic = pfp,
                displayName = user[Users.displayName],
                identifier = user[Users.identifier],
                isAnyAdmin = null,
                isSuperAdmin = user[Users.isSuperAdmin],
                createdAt = user[Users.createdAt],
                updatedAt = user[Users.updatedAt],
                deletedAt = user[Users.deletedAt]
            )
        }
    }
}

suspend fun getUserById(
    id: Int,
    context: CoroutineContext
): User? = newSuspendedTransaction { getUserByCondition(context) { Users.id eq id } }

suspend fun getUserByEmail(
    email: String,
    context: CoroutineContext
): User? = newSuspendedTransaction { getUserByCondition(context) { Users.email eq email } }

suspend fun getUserByIdentifier(
    identifier: String,
    context: CoroutineContext
): User? = newSuspendedTransaction { getUserByCondition(context) { Users.identifier eq identifier } }

suspend fun insertUser(
    user: User,
    context: CoroutineContext
): Int = withContext(context) {
    val code = getCodeByCode(user.registrationCode.code)
    if(
        code != null &&
        getUserByIdentifier(user.identifier, context) == null
    ) transaction {
        Users.insert {
            it[registrationCodeId] = code.id!!
            it[email] = user.email
            it[password] = user.password
            it[profilePicURI] = user.profilePic?.uri
            it[displayName] = user.displayName
            it[identifier] = user.identifier
            it[isSuperAdmin] = user.isSuperAdmin
        }[Users.id]
    } else -1
}

suspend fun <T> modifyUser(
    userId: Int,
    property: Column<T>,
    newValue: T,
    context: CoroutineContext
): Boolean = withContext(context) {
    val user = getUserById(userId, context)
    if (user != null) transaction {
        Users.update({ Users.id eq user.id!! }) {
            it[property] = newValue
        } == 1
    } else false
}

@OptIn(ExperimentalUuidApi::class)
suspend fun createInternalUser(context: CoroutineContext): User =
    withContext(context) {
        newSuspendedTransaction { 
            val user = User(
                id = null,
                registrationCode = getCodeByCode("996633")!!,
                email = "${Uuid.random().toString().substring(0..8)}@example.com",
                _password = Uuid.random().toString().substring(0..8),
                profilePic = null,
                displayName = Uuid.random().toString().substring(0..8),
                identifier = Uuid.random().toString().substring(0..8),
                isAnyAdmin = true,
                isSuperAdmin = false,
                createdAt = null,
                updatedAt = null,
                deletedAt = null
            )
            insertUser(user, context)
            user
        }
    }