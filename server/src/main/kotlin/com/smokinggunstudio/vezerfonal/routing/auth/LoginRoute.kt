package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.v1.jdbc.Database

fun Route.loginRoute(mainDB: Database) {
    post("/basic") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val user = principal.user
        val db = principal.db
        
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
        else null // "No token for you :("
        
        call.respond(TokenResponse(accessToken, refreshToken))
    }
}