package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.ProfileImage
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Users
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllUsers(context: CoroutineContext): List<User> = withContext(context) {
    val codes = getAllCodes(context)
    return@withContext transaction {
        val users = Users.selectAll()
        return@transaction users.map { user ->
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
    condition: (User) -> Boolean
): User? = getAllUsers(context).firstOrNull(condition)

suspend fun getUsersByCondition(
    context: CoroutineContext,
    condition: (User) -> Boolean
): List<User> = getAllUsers(context).filter(condition)

suspend fun getUserById(
    id: Int,
    context: CoroutineContext
): User? = getUserByCondition(context) { user -> user.id == id }

suspend fun getUserByEmail(
    email: String,
    context: CoroutineContext
): User? = getUserByCondition(context) { user -> user.email == email }

suspend fun getUserByIdentifier(
    identifier: String,
    context: CoroutineContext
): User? = getUserByCondition(context) { user -> user.identifier == identifier }