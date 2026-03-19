package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.ChangePasswordRequest
import com.smokinggunstudio.vezerfonal.data.PushToken
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.PushTokenRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.hashLongString
import com.smokinggunstudio.vezerfonal.security.hashPassword
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun Route.userRoute() {
    get("/data") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val user = tryInternal("Unable to query users table.") {
            with(UserRepository(db).getUserById(userId)!!) {
                isAnyAdmin = GroupRepository(db).getGroupsByAdminId(id!!).isNotEmpty() || isSuperAdmin
                return@with this.toDTO()
            }
        } ?: return@get
        
        call.respond(user)
    }
    
    get("/all") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val users = tryInternal("Unable to get users.") {
            UserRepository(db)
                .getAllUsers()
                .map { it.toDTO() }
        } ?: return@get
        
        call.respond(users)
    }
    
    post("/by-identifier-list") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        val identifiers = tryIncoming("Unable to receive identifiers.") {
            call.receive<List<Identifier>>()
        } ?: return@post call.respond(HttpStatusCode.BadRequest)
        
        val users = tryInternal("Unable to get any user.") {
            identifiers.mapNotNull {
                UserRepository(db)
                    .getUserByExternalId(it)
                    ?.toDTO()
            }.ifEmpty { null }
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        call.respond(users)
    }
    
    post("/register-push-token") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        val user = principal.user
        
        val pushToken = tryIncoming("Unable to receive push token.") {
            call.receive<PushToken>()
        } ?: return@post
        
        val success = tryInternal("Unable to insert push token.") {
            PushTokenRepository(db)
                .registerToken(
                    userId = user.id!!,
                    token = pushToken.token,
                    platform = pushToken.platform
                )
        } ?: return@post
        
        if (success) call.respond(HttpStatusCode.OK)
    }

    post("/password-change/request") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db

        val code = Random.nextInt(100_000, 999_999)

        val success = tryInternal("Unable to store password change code.") {
            UserRepository(db).setPasswordChangeCode(userId, hashLongString(code.toString()))
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        if (!success) return@post call.respond(HttpStatusCode.InternalServerError)

        EmailService.sendVerificationCode(principal.user.email, code)
        call.respond(HttpStatusCode.OK)
    }

    put("/password-change") {
        val principal = call.principal<AuthResponse>()
            ?: return@put call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db
        val repo = UserRepository(db)

        val request = tryIncoming("Unable to receive password change request.") {
            call.receive<ChangePasswordRequest>()
        } ?: return@put call.respond(HttpStatusCode.BadRequest)

        if (request.newPassword.length < 8)
            return@put call.respond(HttpStatusCode.BadRequest)

        val storedHash = tryInternal("Unable to retrieve password change code.") {
            repo.getPasswordChangeCodeHash(userId)
        } ?: return@put call.respond(HttpStatusCode.InternalServerError)

        if (storedHash != hashLongString(request.code.toString()))
            return@put call.respond(HttpStatusCode.Forbidden)

        val success = tryInternal("Unable to update password.") {
            repo.updatePassword(userId, hashPassword(request.newPassword))
                .also { repo.setPasswordChangeCode(userId, null) }
        } ?: return@put call.respond(HttpStatusCode.InternalServerError)

        if (success) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    post("/2fa/request") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db

        val code = Random.nextInt(100_000, 999_999)

        val stored = tryInternal("Unable to store 2FA code.") {
            UserRepository(db).setTwoFactorCode(userId, hashLongString(code.toString()))
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        if (!stored) return@post call.respond(HttpStatusCode.InternalServerError)

        EmailService.sendVerificationCode(principal.user.email, code)
        call.respond(HttpStatusCode.OK)
    }

    post("/2fa/enable") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db
        val repo = UserRepository(db)

        val request = tryIncoming("Unable to receive code.") {
            call.receive<Int>()
        } ?: return@post call.respond(HttpStatusCode.BadRequest)

        val storedHash = tryInternal("Unable to retrieve 2FA code.") {
            repo.getTwoFactorCodeHash(userId)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        if (storedHash != hashLongString(request.toString()))
            return@post call.respond(HttpStatusCode.Forbidden)

        val success = tryInternal("Unable to enable 2FA.") {
            repo.setTwoFactorCode(userId, null)
                .also { repo.setTwoFactorEnabled(userId, true) }
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        if (success) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    delete("/2fa") {
        val principal = call.principal<AuthResponse>()
            ?: return@delete call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db

        val success = tryInternal("Unable to disable 2FA.") {
            UserRepository(db).setTwoFactorEnabled(userId, false)
        } ?: return@delete call.respond(HttpStatusCode.InternalServerError)

        if (success) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }

    @OptIn(ExperimentalTime::class)
    delete("/account") {
        val principal = call.principal<AuthResponse>()
            ?: return@delete call.respond(HttpStatusCode.Unauthorized)

        val userId = principal.user.id!!
        val db = principal.db

        val success = tryInternal("Unable to request account deletion.") {
            UserRepository(db).modifyUser(
                userId,
                Users.deletedAt,
                Clock.System.now().toOffsetDateTime(ZoneOffset.UTC)
            )
        } ?: return@delete call.respond(HttpStatusCode.InternalServerError)

        if (success) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.NotFound)
    }
}