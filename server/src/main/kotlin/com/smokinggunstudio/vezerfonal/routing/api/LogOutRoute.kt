package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.objects.JWTs
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.PushTokenRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

suspend fun RoutingContext.logoutRoute() {
    val principal = call.principal<AuthResponse>()
        ?: return call.respond(HttpStatusCode.Unauthorized)
    
    val userId = principal.user.id!!
    val db = principal.db
    
    val success = tryInternal("Cannot log out user.") {
        JWTRepository(db)
            .invalidateAllTokensByUserId(userId)
                &&
        PushTokenRepository(db).invalidateTokensForUser(userId)
    } ?: return
    
    if (success) call.respond(HttpStatusCode.OK)
}