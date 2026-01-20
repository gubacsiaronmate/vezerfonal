package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.toTag
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.TagRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.tagRoute() {
    get("/all") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val tags = tryInternal("Unable to get any tag.") {
            TagRepository(db)
                .getAllTags()
                .map { it.toDTO() }
        } ?: return@get
        
        call.respond(tags)
    }
    
    post("/create") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val tag = tryIncoming("Unable to receive tag.") {
            call.receive<TagData>().toTag()
        } ?: return@post call.respond(HttpStatusCode.BadRequest)
        
        val success = tryInternal("Failed to create tag.") {
            TagRepository(db)
                .insertTag(tag)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    post("/update") {
    
    }
}