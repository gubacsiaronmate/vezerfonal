package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.FileMetaData
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.toDTO
import com.smokinggunstudio.vezerfonal.helpers.toOrganisation
import com.smokinggunstudio.vezerfonal.helpers.toUser
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.v1.jdbc.Database

fun Route.registerRoute(
    mainDB: Database,
) {
    var db: Database? = null
    
    post("/create-org") {
        val org = tryIncoming("Unable to receive organisation data.") {
            call
                .receive<OrgData>()
                .toOrganisation()
        } ?: return@post
        
        val success = tryInternal("Unable to insert org to db.") {
            OrganisationRepository(mainDB).insertOrg(org)
        } ?: return@post
        
        if (!success)
            return@post call.respond(HttpStatusCode.InternalServerError)
        
        val orgDB = ensureOrgDB(org.name)
            ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        db = orgDB
        
        call.respond(HttpStatusCode.OK)
    }
    
    route("/basic") {
        post {
            var rememberMe = false
            
            val pair = tryIncoming("Unable to receive user.") {
                val data = call.receive<String>()
                val userData = data.split('|', limit = 2).first()
                rememberMe = data.split('|', limit = 2).last().toBoolean()
                userData.toDTO<UserData>().toUser(mainDB, db)
            } ?: return@post
            
            if (db == null) db = pair.first
            val user = pair.second
            
            val urepo = UserRepository(db)
            
            val success = tryInternal("Failed to insert user.") {
                urepo.insertUser(user)
            } ?: return@post
            
            val accessToken = tryInternal("Cannot generate jwt.") {
                JWTConfig.generateToken(
                    userExtId = user.externalId,
                    db = db,
                    mainDB = mainDB
                )
            } ?: return@post
            
            val refreshToken = if (rememberMe)
                tryInternal("Cannot generate jwt") {
                    JWTConfig.generateToken(
                        userExtId = user.externalId,
                        db = db,
                        mainDB = mainDB,
                        isRefresh = true
                    )
                }
            else null // "No token for you :("
            
            if (success) call.respond(TokenResponse(accessToken, refreshToken))
        }
    }
}