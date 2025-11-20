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

suspend fun getAllUsers(context: CoroutineContext): List<User> = withContext(context) {
    val codes = getAllCodes(context)
    transaction {
        val users = Users.selectAll()
        users.map { user ->
            val pfp = user[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
            val regCode = codes.first { code -> code.id == user[Users.registrationCodeId] }
            User(
                id = user[Users.id],
                registrationCode = regCode,
                email = user[Users.email],
                _password = user[Users.password],
                profilePic = pfp,
                displayName = user[Users.displayName],
                identifier = user[Users.identifier],
                isSuperAdmin = user[Users.isSuperAdmin],
                createdAt = user[Users.createdAt],
                updatedAt = user[Users.updatedAt],
                deletedAt = user[Users.deletedAt]
            )
        }
    }
}

suspend fun getUserByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): User? = withContext(context) {
    val codes = getAllCodes(context)
    Users.select(condition).firstOrNull()?.let { user ->
        val pfp = user[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
        val regCode = codes.first { code -> code.id == user[Users.registrationCodeId] }
        User(
            id = user[Users.id],
            registrationCode = regCode,
            email = user[Users.email],
            _password = user[Users.password],
            profilePic = pfp,
            displayName = user[Users.displayName],
            identifier = user[Users.identifier],
            isSuperAdmin = user[Users.isSuperAdmin],
            createdAt = user[Users.createdAt],
            updatedAt = user[Users.updatedAt],
            deletedAt = user[Users.deletedAt]
        )
    }
}

suspend fun getUsersByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<User> = withContext(context) {
    val codes = getAllCodes(context)
    Users.select(condition).map { user ->
        val pfp = user[Users.profilePicURI].let { ProfileImage(it, it?.substringAfterLast("/")) }
        val regCode = codes.first { code -> code.id == user[Users.registrationCodeId] }
        User(
            id = user[Users.id],
            registrationCode = regCode,
            email = user[Users.email],
            _password = user[Users.password],
            profilePic = pfp,
            displayName = user[Users.displayName],
            identifier = user[Users.identifier],
            isSuperAdmin = user[Users.isSuperAdmin],
            createdAt = user[Users.createdAt],
            updatedAt = user[Users.updatedAt],
            deletedAt = user[Users.deletedAt]
        )
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
): Boolean = withContext(context) {
    val code = getCodeByCode(user.registrationCode.code, context)
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
        }.insertedCount == 1
    } else false
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

suspend fun createInternalUser(context: CoroutineContext): User = withContext(context) {

}