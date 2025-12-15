package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.FileMetaData
import com.smokinggunstudio.vezerfonal.helpers.ImageService
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
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
import kotlin.coroutines.CoroutineContext

fun Route.registerRoute(
    imageService: ImageService,
    context: CoroutineContext,
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
        
        if (!success) return@post call.respond(HttpStatusCode.InternalServerError)
        
        val orgDB = ensureOrgDB(org.name)
            ?: return@post call.respond(HttpStatusCode.InternalServerError)
        
        db = orgDB
        
        call.respond(HttpStatusCode.OK)
    }
    
    route("/basic") {
        post {
            val pair = tryIncoming("Unable to receive user.") {
                call
                    .receive<UserData>()
                    .toUser(mainDB, db)
            } ?: return@post
            
            if (db == null) db = pair.first
            val user = pair.second
            
            val urepo = UserRepository(db)
            
            val insertSuccess = tryInternal("Failed to insert user.")
            { urepo.insertUser(user) } ?: return@post
            
            if (insertSuccess) {
                val user = urepo.getUserByIdentifier(user.identifier)
                    ?: error("Cannot get user by identifier.")
                val userId = user.id ?: error("Cannot get user id.")
                call.respondText("$userId", status = HttpStatusCode.Created)
            } else call.respondText(
                "Failed to insert user into the database.",
                status = HttpStatusCode.InternalServerError
            )
        }
        
        route("/pfp/{userId}/{rememberMe}") {
            var userId: Int? = null
            var rememberMe: Boolean? = null
            var metadata: FileMetaData? = null
            
            post("/metadata") {
                userId = call.parameters["userId"]?.toIntOrNull()
                    ?: return@post call.respondText(
                        "Invalid user ID",
                        status = HttpStatusCode.BadRequest
                    )
                
                rememberMe = call.parameters["rememberMe"]?.toBooleanStrictOrNull()
                    ?: return@post call.respondText(
                        "Invalid remember me parameter.",
                        status = HttpStatusCode.BadRequest
                    )
                
                metadata = tryIncoming("Unable to receive image metadata.") {
                    call.receive<FileMetaData>()
                } ?: return@post
                
                call.respondText("Accepted", status = HttpStatusCode.Accepted)
            }
            
            post("/filedata") {
                when {
                    userId == null -> error("User Id cannot be null.")
                    metadata == null -> error("Metadata cannot be null.")
                    rememberMe == null -> error("Remember Me cannot be null.")
                }
                
                val data = tryIncoming("Unable to receive image bytes.")
                { call.receiveMultipart(formFieldLimit = 100 * 1024 * 1024L) } ?: return@post
                
                val pfpURI = tryInternal("Failed to save image.") {
                    imageService.saveImage(
                        multipart = data,
                        userId = userId,
                        context = context,
                        extension = metadata.fileType
                    )
                } ?: return@post
                
                val urepo = UserRepository(db!!)
                
                val updateSuccess = tryInternal("Failed to add picture to user.")
                {
                    urepo.modifyUser(
                        userId = userId,
                        property = Users.profilePicURI,
                        newValue = pfpURI,
                    )
                } ?: return@post
                
                if (!updateSuccess) call.respondText(
                    "Failed to save picture to database.",
                    status = HttpStatusCode.InternalServerError
                )
                
                val accessToken = tryInternal("Cannot generate jwt.") {
                    JWTConfig.generateToken(
                        userId = userId,
                        db = db,
                        mainDB = mainDB
                    )
                } ?: return@post
                
                val refreshToken = tryInternal("Cannot generate jwt") {
                    JWTConfig.generateToken(
                        userId = userId,
                        db = db,
                        mainDB = mainDB,
                        isRefresh = true
                    )
                } ?: return@post
                
                call.respond(
                    TokenResponse(
                        accessToken,
                        if (rememberMe)
                            refreshToken
                        else null // "No token for you :("
                    )
                )
            }
        }
    }
}