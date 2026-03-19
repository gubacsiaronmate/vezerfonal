package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.toRegCode
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.RegistrationCodeRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import org.jetbrains.exposed.v1.jdbc.Database

fun Route.codeRoute(mainDB: Database) {
    get("/all") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            return@get call.respond(HttpStatusCode.Forbidden)

        val orgName = principal.db
            .connector()
            .schema
            .trim()
            .removePrefix("vezerfonal_org_")
            .lowercase()
        
        log { orgName }
        
        val codes = tryInternal("Unable to get codes.") {
            RegistrationCodeRepository(mainDB)
                .getAllCodes()
                .filter {
                    it.organisation.name.lowercase() == orgName
                }.map { it.toDTO() }
        } ?: return@get call.respond(HttpStatusCode.InternalServerError)
        
        call.respond(codes)
    }
    
    post("/create") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            return@post call.respond(HttpStatusCode.Forbidden)

        val regCode = tryIncoming("Unable to receive code.")
        { call.receive<RegCodeData>().toRegCode(principal.org) } ?: return@post call.respond(HttpStatusCode.BadRequest)

        val success = tryInternal("Unable to insert reg code") {
            RegistrationCodeRepository(mainDB)
                .insertCode(regCode)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    patch("/update") {
        val principal = call.principal<AuthResponse>()
            ?: return@patch call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            return@patch call.respond(HttpStatusCode.Forbidden)

        val org = principal.org

        val newCode = tryIncoming("Unable to receive code.") {
            call.receive<RegCodeData>().toRegCode(org)
        } ?: return@patch call.respond(HttpStatusCode.BadRequest)

        val success = tryInternal("Unable to update code.") {
            RegistrationCodeRepository(mainDB)
                .updateCode(newCode)
        } ?: return@patch call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    delete("/delete") {
        val principal = call.principal<AuthResponse>()
            ?: return@delete call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            return@delete call.respond(HttpStatusCode.Forbidden)

        val code = tryIncoming("Unable to receive code.") {
            call.receive<String>()
        } ?: return@delete call.respond(HttpStatusCode.BadRequest)

        val success = tryInternal("Unable to delete code.") {
            RegistrationCodeRepository(mainDB).deleteCode(code)
        } ?: return@delete call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
}