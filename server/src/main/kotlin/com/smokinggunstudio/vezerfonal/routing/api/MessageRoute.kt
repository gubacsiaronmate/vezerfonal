package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.models.InteractionInfo
import com.smokinggunstudio.vezerfonal.models.User
import com.smokinggunstudio.vezerfonal.repositories.InteractionInfoRepository
import com.smokinggunstudio.vezerfonal.repositories.MessageRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Route.messageRoute() {
    get("/subscribe") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        
        call.response.cacheControl(CacheControl.NoCache(null))
        call.respondTextWriter(ContentType.Text.EventStream) {
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
        
        val user = principal.user
        val db = principal.db
        
        val amount = call.parameters["amount"]?.toIntOrNull()
        
        val messages = tryInternal("Unable to get messages.") {
            MessageRepository(db)
                .getNonArchivedMessagesByRecipientUserId(user.id!!, limit = amount)
                .fillMissingInfos(db, user.id, user.isAnyAdmin == true)
        } ?: return@get
        
        call.respond(messages)
    }
    
    get("/sent/{amount}") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val amount = call.parameters["amount"]?.toIntOrNull()
        
        val user = principal.user
        
        if (user.isAnyAdmin != true)
            return@get call.respond(HttpStatusCode.Forbidden)
        
        val db = principal.db
        
        val messages = tryInternal("Unable to get messages.") {
            MessageRepository(db)
                .getMessagesBySenderUserId(user.id!!, limit = amount)
                .fillMissingInfos(db, user.id, user.isAnyAdmin == true)
        } ?: return@get call.respond(HttpStatusCode.InternalServerError)
        
        call.respond(messages)
    }
    
    post("/send") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val message = tryInternal("Unable to receive message.") {
            call
                .receive<MessageData>()
                .toMessage(db)
        } ?: return@post
        
        val (id, success) = tryInternal("Unable to insert message.") {
             MessageRepository(db).insertMessage(message)
        } ?: return@post
        
        if (!success) call.respondText(
            text = "Message already sent.",
            status = HttpStatusCode.TooManyRequests
        )
        
        val recipients: List<User> = message.user?.let { listOf(it) }
            ?: message.group!!.members.map { it.user }
        
        val insertedMessage = tryInternal("Unable to get message.") {
            MessageRepository(db).getMessageById(id)
        } ?: return@post
        
        MessageHub.broadcast(insertedMessage, db)
        
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
        
        if (interactionSuccess.all { it })
            call.respond(HttpStatusCode.OK)
        else call.respond(HttpStatusCode.InternalServerError)
    }
    
    get("/archived/{amount}") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val amount = call.parameters["amount"]?.toIntOrNull()
        
        val user = principal.user
        val db = principal.db
        
        val messages = tryInternal("Unable to get messages") {
            MessageRepository(db)
                .getArchivedMessagesByRecipientUserId(user.id!!, limit = amount)
                .fillMissingInfos(db, user.id, user.isAnyAdmin == true)
        } ?: return@get
        
        call.respond(messages)
    }
    
    route("/interactions", Route::interactionRoute)
}