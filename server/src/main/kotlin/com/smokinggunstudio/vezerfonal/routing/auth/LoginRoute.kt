package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

fun Route.loginRoute(context: CoroutineContext, mainDB: Database) {
    post("/basic") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val jrepo = JWTRepository(db)
        val activeTokens = jrepo.getActiveJWTsByUserId(userId)
        
        if (activeTokens.isNotEmpty())
            tryInternal("Unable to invalidate old tokens.") {
                activeTokens.forEach { token ->
                    jrepo.invalidateToken(token.id)
                }
            }
        
        val accessToken = tryInternal("Cannot generate jwt") {
            JWTConfig.generateToken(
                userId = userId,
                db = db,
                mainDB = mainDB
            )
        } ?: return@post
        
        val refreshToken = tryInternal("Cannot generate jwt") {
            JWTConfig.generateToken(
                userId = userId,
                db = db,
                mainDB = mainDB,
                isRefresh = true
            )
        } ?: return@post
        
        val newToken = TokenResponse(
            accessToken,
            if (principal.rememberMe) refreshToken
            else null // "No token for you :("
        )
        
        call.respond(newToken)
    }
}