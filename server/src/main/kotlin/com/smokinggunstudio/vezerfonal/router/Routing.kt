package com.smokinggunstudio.vezerfonal.router

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.getUserByIdentifier
import com.smokinggunstudio.vezerfonal.repositories.insertUser
import com.smokinggunstudio.vezerfonal.repositories.modifyUser
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.auth.configureBasicAuth
import com.smokinggunstudio.vezerfonal.security.auth.configureJWTAuth
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlin.coroutines.CoroutineContext

fun Application.configureRouting(imageService: ImageService, context: CoroutineContext) {
    install(ContentNegotiation) { json() }
    
    authentication {
        configureBasicAuth(this, context)
        configureJWTAuth(this, context)
    }
    
    routing { route("/api") {
        route("/register") {
            post("/basic") {
                val user = tryIncoming("Unable to receive user.")
                { call.receive<UserData>().toUser(context) } ?: return@post
                
                val insertSuccess = tryOutgoing("Failed to insert user.")
                { insertUser(user, context) } ?: return@post
                
                if (insertSuccess) {
                    val user = getUserByIdentifier(user.identifier, context)
                        ?: error("Inside insertSuccess if block user is null.")
                    val userId = user.id ?: error("Inside insertSuccess if block userId is null.")
                    call.respondText("$userId", status = HttpStatusCode.Created)
                } else call.respondText(
                    "Failed to insert user into the database.",
                    status = HttpStatusCode.InternalServerError
                )
            }
            
            post("/basic/pfp/{userId}") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                    ?: run { call.respondText(
                        "Invalid user ID",
                        status = HttpStatusCode.BadRequest
                    ); return@post }
                
                val data = tryIncoming("Unable to receive image (Multipart).")
                { call.receiveChannel().toByteArray() } ?: return@post
                
                val pfpURI = tryOutgoing("Failed to save image.")
                { imageService.saveImageBytes(data, userId, context) } ?: return@post
                
                val updateSuccess = tryOutgoing("Failed to add picture to user.")
                { modifyUser(
                    userId = userId,
                    property = Users.profilePicURI,
                    newValue = pfpURI,
                    context = context
                ) } ?: return@post
                
                if (!updateSuccess) call.respondText(
                    "Failed to save picture to database.",
                    status = HttpStatusCode.InternalServerError
                )
                
                val jwt = tryOutgoing("Cannot generate jwt.")
                { JWTConfig.generateToken(userId, context) } ?: return@post
                
                call.respond(mapOf("token" to jwt))
            }
            
            route("/oauth") {
            
            }
        }
        
        route("/login") {
            authenticate("basic") { route("/basic") {
                post {
                    val principal = call.principal<AuthResponse>()
                    val id: Int = principal?.userId
                        ?: return@post call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
                    print(id)
                }
            } }
            
//            authenticate("oauth") { route("/oauth") {
//                print("asd")
//            } }
        }
    } }
}