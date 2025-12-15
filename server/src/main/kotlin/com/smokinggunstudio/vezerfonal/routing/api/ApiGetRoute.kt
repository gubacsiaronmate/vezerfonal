package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

suspend fun RoutingContext.apiGetRoute() {
    call.principal<AuthResponse>()
        ?: return call.respond(HttpStatusCode.Unauthorized)
    
    call.respond(HttpStatusCode.OK)
}