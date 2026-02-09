package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.RegCodeData
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
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
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.coroutines.CoroutineContext

fun Route.codeRoute(mainDB: Database) {
    get("/all") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val orgName =
            transaction(principal.db) { TransactionManager.current().connection.schema }
            .trim()
            .removePrefix("vezerfonal_org_")
            .lowercase()
        
        val codes = tryInternal("Unable to get codes.") {
            RegistrationCodeRepository(mainDB)
                .getAllCodes()
                .filter {
                    it.organisation.name.lowercase() != orgName
                }.map { it.toDTO() }
        } ?: return@get
        
        call.respond(codes)
    }
    
    post("/create") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val regCode = tryIncoming("Unable to receive code.")
        { call.receive<RegCodeData>().toRegCode(principal.org) } ?: return@post
        
        val success = tryInternal("Unable to insert reg code") {
            RegistrationCodeRepository(mainDB)
                .insertCode(regCode)
        } ?: return@post
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    patch("/update") {
        val principal = call.principal<AuthResponse>()
            ?: return@patch call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        val org = principal.org
        
        val newCode = tryInternal("Unable to receive code.") {
            call.receive<RegCodeData>().toRegCode(org)
        } ?: return@patch call.respond(HttpStatusCode.BadRequest)
        
        val success = tryInternal("Unable to update code.") {
            RegistrationCodeRepository(db)
                .updateCode(newCode)
        } ?: return@patch call.respond(HttpStatusCode.InternalServerError)
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    delete("/delete") {
        val principal = call.principal<AuthResponse>()
            ?: return@delete call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val code = tryIncoming("Unable to receive code.") {
            call.receive<String>()
        } ?: return@delete
        
        val success = tryInternal("Unable to delete code.") {
            RegistrationCodeRepository(db).deleteCode(code)
        } ?: return@delete
        
        if (success) call.respond(HttpStatusCode.OK)
    }
}