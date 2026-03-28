package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.EmailService
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.hashLongString
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.random.Random

fun Route.loginRoute(mainDB: Database) {
    post("/basic") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val user = principal.user
        val db = principal.db

        if (user.twoFactorEnabled) {
            val code = Random.nextInt(100_000, 999_999)
            val stored = tryInternal("Unable to store 2FA code.") {
                UserRepository(db).setTwoFactorCode(user.id!!, hashLongString(code.toString()))
            } ?: return@post call.respond(HttpStatusCode.InternalServerError)
            if (!stored) return@post call.respond(HttpStatusCode.InternalServerError)
            EmailService.sendVerificationCode(user.email, code)
            return@post call.respond(HttpStatusCode.Accepted)
        }

        val success = tryInternal("Unable to invalidate old tokens.") {
            JWTRepository(db)
                .invalidateAllTokensByUserId(user.id!!)
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)

        if (!success)
            return@post call.respond(HttpStatusCode.InternalServerError)

        val accessToken = tryInternal("Cannot generate jwt") {
            JWTConfig.generateToken(
                userExtId = user.externalId,
                db = db,
                mainDB = mainDB
            )
        } ?: return@post

        val refreshToken = if (principal.rememberMe)
            tryInternal("Cannot generate jwt") {
                JWTConfig.generateToken(
                    userExtId = user.externalId,
                    db = db,
                    mainDB = mainDB,
                    isRefresh = true
                )
            }
        else null

        call.respond(TokenResponse(accessToken, refreshToken))
    }
}
