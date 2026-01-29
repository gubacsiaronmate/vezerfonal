package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.PushToken
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.PushTokenRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute() {
    get("/data") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val user = tryInternal("Unable to query users table.") {
            with(UserRepository(db).getUserById(userId)!!) {
                isAnyAdmin = GroupRepository(db).getGroupsByAdminId(id!!).isNotEmpty() || isSuperAdmin
                log { "isAnyAdmin: $isAnyAdmin" }
                return@with this.toDTO()
            }
        } ?: return@get
        
        call.respond(user)
        log { "Responded with ${user.name}\nisAnyAdmin: ${user.isAnyAdmin}" }
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
}