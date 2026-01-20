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
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlin.coroutines.Continuation

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
            call.respond(HttpStatusCode.Forbidden)
        
        val db = principal.db
        
        val tag = tryIncoming("Unable to receive tag.") {
            call.receive<TagData>().toTag()
        } ?: return@post call.respond(HttpStatusCode.BadRequest)
        
        val success = tryInternal("Failed to create tag.") {
            TagRepository(db).insertTag(tag)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    put("/update") {
        val principal = call.principal<AuthResponse>()
            ?: return@put call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Forbidden)
        
        val db = principal.db
        
        val tag = tryIncoming("Unable to receive tag.") {
            call.receive<TagData>().toTag()
        } ?: return@put call.respond(HttpStatusCode.BadRequest)
        
        val success = tryInternal("Failed to update tag.") {
            TagRepository(db).modifyTag(tag)
        } ?: return@put call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    delete("/delete") {
        val principal = call.principal<AuthResponse>()
            ?: return@delete call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Forbidden)
        
        val db = principal.db
        
        val tag = tryIncoming("Unable to receive tag.") {
            call.receive<TagData>().toTag()
        } ?: return@delete call.respond(HttpStatusCode.InternalServerError)
        
        val success = tryInternal("Failed to delete tag.") {
            TagRepository(db).deleteTag(tag)
        } ?: return@delete call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
}