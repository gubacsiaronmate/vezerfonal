package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.TagRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

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
}