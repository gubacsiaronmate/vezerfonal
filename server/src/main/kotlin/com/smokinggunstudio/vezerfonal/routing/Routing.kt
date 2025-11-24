package com.smokinggunstudio.vezerfonal.routing

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.Users
import com.smokinggunstudio.vezerfonal.repositories.*
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
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

fun Application.configureRouting(imageService: ImageService, context: CoroutineContext) {
    install(ContentNegotiation) { json() }
    
    authentication {
        configureBasicAuth(this, context)
        configureJWTAuth(this, context)
//        configureOAuth(this, context)
    }
    
    routing {
        get("/") { call.respondText("Hello") }
        
        route("/register") {
            post("/basic") {
                val user = tryIncoming("Unable to receive user.")
                { call.receive<UserData>().toUser(context) } ?: return@post
                
                val insertSuccess = tryInternal("Failed to insert user.")
                { insertUser(user, context) != -1 } ?: return@post
                
                if (insertSuccess) {
                    val user = getUserByIdentifier(user.identifier, context)
                        ?: error("Cannot get user by identifier.")
                    val userId = user.id ?: error("Cannot get user id.")
                    call.respondText("$userId", status = HttpStatusCode.Created)
                } else call.respondText(
                    "Failed to insert user into the database.",
                    status = HttpStatusCode.InternalServerError
                )
            }
            
            route("/basic/pfp/{userId}/{rememberMe}") {
                var userId: Int? = null
                var rememberMe: Boolean? = null
                var metadata: FileMetaData? = null
                
                post("/metadata") {
                    userId = call.parameters["userId"]?.toIntOrNull()
                        ?: run {
                            call.respondText(
                                "Invalid user ID",
                                status = HttpStatusCode.BadRequest
                            ); return@post
                        }
                    
                    rememberMe = call.parameters["rememberMe"]?.toBooleanStrictOrNull()
                        ?: run {
                            call.respondText(
                                "Invalid remember me parameter.",
                                status = HttpStatusCode.BadRequest
                            ); return@post
                        }
                    
                    metadata = tryIncoming("Unable to receive image metadata.")
                    { call.receive<FileMetaData>() } ?: return@post
                    
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
                    
                    val pfpURI = tryInternal("Failed to save image.")
                    { imageService.saveImage(data, userId, context, extension = metadata.fileType) } ?: return@post
                    
                    val updateSuccess = tryInternal("Failed to add picture to user.")
                    {
                        modifyUser(
                            userId = userId,
                            property = Users.profilePicURI,
                            newValue = pfpURI,
                            context = context
                        )
                    } ?: return@post
                    
                    if (!updateSuccess) call.respondText(
                        "Failed to save picture to database.",
                        status = HttpStatusCode.InternalServerError
                    )
                    
                    val accessToken = tryInternal("Cannot generate jwt.")
                    { JWTConfig.generateToken(userId, context) } ?: return@post
                    
                    val refreshToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context, isRefresh = true) } ?: return@post
                    
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
            
            route("/oauth") {
            
            }
        }
        
        authenticate("jwt-refresh") {
            post("/refresh") {
                val principal = call.principal<AuthResponse>()
                val id = principal?.userId
                    ?: return@post call.respondText(
                        "Unauthorized",
                        status = HttpStatusCode.Unauthorized
                    )
                
                val accessToken = tryInternal("Cannot generate jwt")
                { JWTConfig.generateToken(id, context) } ?: return@post
                
                val refreshToken = tryInternal("Cannot generate jwt")
                { JWTConfig.generateToken(id, context, isRefresh = true) } ?: return@post
                
                call.respond(TokenResponse(accessToken, refreshToken))
            }
        }
        
        route("/login") {
            authenticate("basic") {
                post("/basic") {
                    val principal = call.principal<AuthResponse>()
                    val userId: Int = principal?.userId
                        ?: return@post call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    val activeTokens = getActiveJWTsByUserId(userId, context).latestPair()
                    
                    if (activeTokens != null) return@post call.respond(activeTokens)
                    
                    val accessToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context) } ?: return@post
                    
                    val refreshToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context, isRefresh = true) } ?: return@post
                    
                    val newToken = TokenResponse(
                        accessToken,
                        if (principal.rememberMe) refreshToken
                        else null // "No token for you :("
                    )
                    
                    call.respond(newToken)
                }
            }


//            authenticate("oauth") { post("/oauth") {
//                print("asd")
//            } }
        }
        
        authenticate("jwt-access") {
            route("/api") {
                get {
                    val principal = call.principal<AuthResponse>()
                    if (principal == null) call.respondText(
                        text = "Unauthorized.",
                        status = HttpStatusCode.Unauthorized
                    )
                    else call.respondText(
                        text = "Authorized",
                        status = HttpStatusCode.OK
                    )
                }
                
                route("/messages") {
                    get("/subscribe") {
                        val principal = call.principal<AuthResponse>()
                        val userId = principal?.userId
                            ?: return@get call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        
                        call.response.cacheControl(CacheControl.NoCache(null))
                        call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                            val channel = MessageHub.subscribe(userId)
                            try {
                                for (message in channel) {
                                    write(Json.encodeToString(message) + "\n\n")
                                    flush()
                                }
                            } finally {
                                MessageHub.unsubscribe(userId, channel)
                            }
                        }
                    }
                    
                    get("/{amount}") {
                        val principal = call.principal<AuthResponse>()
                        val userId = principal?.userId
                            ?: return@get call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        val amount = call.parameters["amount"]?.toIntOrNull()
                            ?: return@get
                        
                        val messages = getMessagesByUserId(userId, context, limit = amount).map { it.toDTO() }
                        
                        call.respond(messages)
                    }
                    
                    post("/send") {
                        val principal = call.principal<AuthResponse>()
                        val authorId = principal?.userId
                            ?: return@post call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        
                        val message = tryInternal("Unable to receive message.")
                        { call.receive<MessageData>().toMessage(
                            authorId = authorId,
                            context = context
                        ) } ?: return@post
                        
                        val success = tryInternal("Unable to insert message.")
                        { insertMessage(message, context) } ?: return@post
                        
                        if (!success) call.respondText(
                            text = "Message already sent.",
                            status = HttpStatusCode.TooManyRequests
                        )
                        
                        MessageHub.broadcast(message, context)
                        
                        val recipients: List<User> = (message.user?.let { listOf(it) }
                            ?: message.group!!.members.map { it.user }) + message.author
                        
                        val interactionSuccess = tryInternal("Unable to insert all interactions.")
                        { recipients.map { user ->
                            insertInteraction(
                                InteractionInfo(
                                    message = message,
                                    user = user,
                                    type = InteractionType.status,
                                    status = message.status,
                                ), context
                            )
                        } } ?: return@post
                        
                        if (interactionSuccess.all { it }) call.respond(HttpStatusCode.OK)
                        else call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}
