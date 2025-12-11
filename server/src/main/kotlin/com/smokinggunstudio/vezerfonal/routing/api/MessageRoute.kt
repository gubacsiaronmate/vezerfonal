package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.MessageHub
import com.smokinggunstudio.vezerfonal.helpers.toInteractionInfo
import com.smokinggunstudio.vezerfonal.helpers.toMessage
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.InteractionInfoRepository
import com.smokinggunstudio.vezerfonal.repositories.MessageRepository
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.response.respondTextWriter
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

fun Route.messageRoute(context: CoroutineContext) {
    get("/subscribe") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        val userId = principal.user.id!!
        
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
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val amount = call.parameters["amount"]?.toIntOrNull()
            ?: return@get
        
        val messages = tryInternal("Unable to get messages.") {
            MessageRepository(db)
                .getMessagesByRecipientUserId(userId, limit = amount)
                .map { message ->
                    val interactions = InteractionInfoRepository(db)
                        .getInteractionInfosByMessageAndUserId(message.id!!, userId)
                    val reaction = try {
                        interactions.single { it.type == InteractionType.reaction }.reaction
                    } catch (_: Exception) { null }
                    message.toDTO(reaction)
                }
        } ?: return@get
        
        call.respond(messages)
    }
    
    post("/send") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val message = tryInternal("Unable to receive message.") {
            call
                .receive<MessageData>()
                .toMessage(context, db)
        } ?: return@post
        
        val (id, success) = tryInternal("Unable to insert message.") {
            MessageRepository(db)
                .insertMessage(message)
        } ?: return@post
        
        if (!success) call.respondText(
            text = "Message already sent.",
            status = HttpStatusCode.TooManyRequests
        )
        
        MessageHub.broadcast(message, context)
        
        val recipients: List<User> = message.user?.let { listOf(it) }
            ?: message.group!!.members.map { it.user }
        
        val insertedMessage = tryInternal("Unable to get message.") {
            MessageRepository(db)
                .getMessageById(id)
        } ?: return@post
        
        val interactionSuccess = tryInternal("Unable to insert all interactions.") {
            recipients.map { user ->
                InteractionInfoRepository(db)
                    .insertInteraction(
                        InteractionInfo(
                            message = insertedMessage,
                            user = user,
                            type = InteractionType.status,
                            status = message.status,
                        )
                    )
            }
        } ?: return@post
        
        if (interactionSuccess.all { it }) call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }
    
    route("/interactions") {
        route("/reaction") {
            post("/send") {
                val principal = call.principal<AuthResponse>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                
                val user = principal.user
                val db = principal.db
                
                val interaction = tryIncoming("Unable to receive interaction.") {
                    call
                        .receive<InteractionInfoData>()
                        .toInteractionInfo(user, db, context)
                } ?: return@post
                
                val success = tryInternal("Unable to save interaction.") {
                    InteractionInfoRepository(db)
                        .insertInteraction(interaction)
                } ?: return@post
                
                if (success) call.respond(HttpStatusCode.OK)
            }
        }
    }
}