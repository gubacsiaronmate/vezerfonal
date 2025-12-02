package com.smokinggunstudio.vezerfonal.routing

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.models.Group
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.objects.JWTs
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
import io.ktor.util.reflect.instanceOf
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

fun Application.configureRouting(imageService: ImageService, mainDB: Database, context: CoroutineContext) {
    install(ContentNegotiation) { json() }
    
    authentication {
        configureBasicAuth(this, mainDB, context)
        configureJWTAuth(this, mainDB, context)
//        configureOAuth(this, context)
    }
    
    routing {
        get("/") { call.respondText("Hello") }
        
        route("/register") {
            var db: Database? = null
            
            post("/create-org") {
                val org = tryIncoming("Unable to receive organisation data.")
                { call.receive<OrgData>().toOrganisation() } ?: return@post
                
                val success = tryInternal("Unable to insert org to db.")
                { OrganisationRepository(mainDB).insertOrg(org) } ?: return@post
                
                if (!success) return@post call.respond(HttpStatusCode.InternalServerError)
                
                val orgDB = ensureOrgDB(org.name, context)
                    ?: return@post call.respond(HttpStatusCode.InternalServerError)
                
                db = orgDB
                
                call.respond(HttpStatusCode.OK)
            }
            
            post("/basic") {
                val pair = tryIncoming("Unable to receive user.")
                { call.receive<UserData>().toUser(context, mainDB, db) } ?: return@post
                
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
                    
                    val accessToken = tryInternal("Cannot generate jwt.")
                    { JWTConfig.generateToken(userId, context, db, mainDB) } ?: return@post
                    
                    val refreshToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context, db, mainDB, isRefresh = true) } ?: return@post
                    
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
            
            // TODO? maybe: route("/oauth") { }
        }
        
        authenticate("jwt-refresh") {
            get("/refresh") {
                val principal = call.principal<AuthResponse>()
                    ?: return@get call.respondText(
                        "Unauthorized",
                        status = HttpStatusCode.Unauthorized
                    )
                
                val userId = principal.userId
                val db = principal.db
                
                val accessToken = tryInternal("Cannot generate jwt")
                { JWTConfig.generateToken(userId, context, db, mainDB) } ?: return@get
                
                val refreshToken = tryInternal("Cannot generate jwt")
                { JWTConfig.generateToken(userId, context, db, mainDB, isRefresh = true) } ?: return@get
                
                call.respond(TokenResponse(accessToken, refreshToken))
            }
        }
        
        route("/login") {
            authenticate("basic") {
                post("/basic") {
                    val principal = call.principal<AuthResponse>()
                    ?: return@post call.respondText(
                        "Unauthorized",
                        status = HttpStatusCode.Unauthorized
                    )
                
                    val userId = principal.userId
                    val db = principal.db
                    
                    val activeTokens = JWTRepository(db).getActiveJWTsByUserId(userId).latestPair()
                    
                    if (activeTokens != null) return@post call.respond(activeTokens)
                    
                    val accessToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context, db, mainDB) } ?: return@post
                    
                    val refreshToken = tryInternal("Cannot generate jwt")
                    { JWTConfig.generateToken(userId, context, db, mainDB, isRefresh = true) } ?: return@post
                    
                    val newToken = TokenResponse(
                        accessToken,
                        if (principal.rememberMe) refreshToken
                        else null // "No token for you :("
                    )
                    
                    call.respond(newToken)
                }
            }
        }
        
        authenticate("jwt-access") {
            route("/api") {
                get {
                    call.principal<AuthResponse>()
                        ?: return@get call.respondText(
                            text = "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    call.respond(HttpStatusCode.OK)
                }
                
                route("/messages") {
                    get("/subscribe") {
                        val principal = call.principal<AuthResponse>()
                            ?: return@get call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        val userId = principal.userId
                        
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
                            ?: return@get call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        
                        val userId = principal.userId
                        val db = principal.db
                        
                        val amount = call.parameters["amount"]?.toIntOrNull()
                            ?: return@get
                        
                        val messages = MessageRepository(db).getMessagesBySenderUserId(userId, limit = amount).map { it.toDTO() }
                        
                        call.respond(messages)
                    }
                    
                    post("/send") {
                        val principal = call.principal<AuthResponse>()
                            ?: return@post call.respondText(
                                "Unauthorized",
                                status = HttpStatusCode.Unauthorized
                            )
                        
                        val authorId = principal.userId
                        val db = principal.db
                        
                        val message = tryInternal("Unable to receive message.")
                        { call.receive<MessageData>().toMessage(
                            authorId = authorId,
                            context = context,
                            db = db
                        ) } ?: return@post
                        
                        val success = tryInternal("Unable to insert message.")
                        { MessageRepository(db).insertMessage(message) } ?: return@post
                        
                        if (!success) call.respondText(
                            text = "Message already sent.",
                            status = HttpStatusCode.TooManyRequests
                        )
                        
                        MessageHub.broadcast(message, context)
                        
                        val recipients: List<User> = (message.user?.let { listOf(it) }
                            ?: message.group!!.members.map { it.user }) + message.author
                        
                        val interactionSuccess = tryInternal("Unable to insert all interactions.")
                        { recipients.map { user ->
                            InteractionInfoRepository(db)
                                .insertInteraction(
                                    InteractionInfo(
                                        message = message,
                                        user = user,
                                        type = InteractionType.status,
                                        status = message.status,
                                    )
                                )
                        } } ?: return@post
                        
                        if (interactionSuccess.all { it }) call.respond(HttpStatusCode.OK)
                        else call.respond(HttpStatusCode.InternalServerError)
                    }
                }
                
                get("/user-data") {
                    val principal = call.principal<AuthResponse>()
                        ?: return@get call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    val userId = principal.userId
                    val db = principal.db
                    
                    val user = tryInternal("Unable to query users table.") {
                        UserRepository(db).getUserById(userId)!!.apply {
                            this.isAnyAdmin = GroupRepository(db).getGroupsByAdminId(this.id!!).isNotEmpty()
                        }.toDTO()
                    } ?: return@get
                    
                    call.respond(user)
                }
                
                get("/group-data") {
                    val principal = call.principal<AuthResponse>()
                        ?: return@get call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    val userId = principal.userId
                    val db = principal.db
                    
                    val groups = tryInternal("") {
                        GroupRepository(db)
                            .getAllGroupsByMemberUserId(userId)
                            .map { it.toDTO() }
                    } ?: return@get
                    
                    call.respond(groups)
                }
                
                get("/logout") {
                    val principal = call.principal<AuthResponse>()
                        ?: return@get call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    val userId = principal.userId
                    val db = principal.db
                    
                    val jrepo = JWTRepository(db)
                    
                    val success = tryInternal("Cannot log out user.") {
                        jrepo.getJWTsByUserId(userId).map { jwt ->
                            jrepo.modifyJWT(
                                tokenId = jwt.id,
                                property = JWTs.revoked,
                                newValue = true
                            )
                        }.all { it }
                    } ?: return@get
                    
                    if (success) call.respond(HttpStatusCode.OK)
                }
                
                post("/create-group") {
                    val principal = call.principal<AuthResponse>()
                        ?: return@post call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )
                    
                    val db = principal.db
                    
                    val group = tryIncoming("") {
                        call.receive<GroupData>()
                    } ?: return@post
                }
            }
        }
    }
}
