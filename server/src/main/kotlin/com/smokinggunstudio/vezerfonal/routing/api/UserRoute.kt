package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.userRoute() {
    get("/data") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val user = tryInternal("Unable to query users table.") {
            UserRepository(db).getUserById(userId)!!.apply {
                isAnyAdmin = GroupRepository(db).getGroupsByAdminId(this.id!!).isNotEmpty()
            }.toDTO()
        } ?: return@get
        
        call.respond(user)
        log { "Responded with ${user.name}" }
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
        val identifiers = call.receive<List<Identifier>>()
        
        val users = tryInternal("Unable to get any user.") {
            identifiers.mapNotNull {
                UserRepository(db)
                    .getUserByIdentifier(it)
            }.map { it.toDTO() }
        } ?: return@post
        
        call.respond(users)
    }
}