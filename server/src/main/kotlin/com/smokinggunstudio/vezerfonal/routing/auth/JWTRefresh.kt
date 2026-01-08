package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun RoutingContext.jwtRefresh(
    mainDB: Database
) {
    val principal = call.principal<AuthResponse>()
        ?: return call.respond(HttpStatusCode.Unauthorized)
    
    val user = principal.user
    val db = principal.db
    
    val accessToken = tryInternal("Cannot generate jwt") {
        JWTConfig.generateToken(
            userExtId = user.externalId,
            db = db,
            mainDB = mainDB
        )
    } ?: return
    
    val refreshToken = tryInternal("Cannot generate jwt") {
        JWTConfig.generateToken(
            userExtId = user.externalId,
            db = db,
            mainDB = mainDB,
            isRefresh = true
        )
    } ?: return
    
    call.respond(TokenResponse(accessToken, refreshToken))
}