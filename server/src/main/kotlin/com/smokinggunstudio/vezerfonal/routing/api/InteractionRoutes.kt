package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.data.MessageStatusData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.*
import com.smokinggunstudio.vezerfonal.repositories.InteractionInfoRepository
import com.smokinggunstudio.vezerfonal.repositories.MessageRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.interactionRoute() {
    route("/reaction") {
        get("/by-message-ext-id/{messageExtId}") {
            val principal = call.principal<AuthResponse>()
                ?: return@get call.respond(HttpStatusCode.Unauthorized)
            
            val user = principal.user
            
            if (user.isAnyAdmin != true)
                call.respond(HttpStatusCode.Unauthorized)
            
            val messageExtId = tryIncoming("Unable to receive message extId.") {
                call.parameters["messageExtId"]
            } ?: return@get call.respond(HttpStatusCode.BadRequest)
            
            val db = principal.db
            
            val interactions = tryInternal("Unable to get interactions") {
                val message = MessageRepository(db)
                    .getMessageByExtId(messageExtId)
                    ?: return@tryInternal null
                
                
                InteractionInfoRepository(db)
                    .getInteractionInfosByMessageIdAndType(message.id!!, InteractionType.reaction)
                    .map { it.toDTO() }
            } ?: return@get call.respond(HttpStatusCode.InternalServerError)
            
            val interactionsAndUsers = tryInternal("Unable to get users") {
                interactions.map {
                    val u = UserRepository(db)
                        .getUserByExternalId(it.userIdentifier)
                        ?: return@tryInternal null
                    Pair(u.toDTO(), it)
                }
            } ?: return@get call.respond(HttpStatusCode.InternalServerError)
            
            call.respond(interactionsAndUsers.toListOfDTO())
        }
        
        post("/send") {
            val principal = call.principal<AuthResponse>()
                ?: return@post call.respond(HttpStatusCode.Unauthorized)
            
            val user = principal.user
            val db = principal.db
            
            val interaction = tryIncoming("Unable to receive interaction.") {
                call
                    .receive<InteractionInfoData>()
                    .toInteractionInfo(user, db)
            } ?: return@post
            
            val success = tryInternal("Unable to save interaction.") {
                InteractionInfoRepository(db)
                    .insertInteraction(interaction)
            } ?: return@post
            
            if (success) call.respond(HttpStatusCode.OK)
        }
    }
    
    route("/archive") {
        post("/send") {
            val principal = call.principal<AuthResponse>()
                ?: return@post call.respond(HttpStatusCode.Unauthorized)
            
            val user = principal.user
            val db = principal.db
            
            val interaction = tryIncoming("Unable to receive interaction.") {
                call
                    .receive<InteractionInfoData>()
                    .toInteractionInfo(user, db)
            } ?: return@post call.respond(HttpStatusCode.InternalServerError)
            
            val success = tryInternal("Unable to insert interaction.") {
                InteractionInfoRepository(db).insertInteraction(interaction)
            } ?: return@post call.respond(HttpStatusCode.InternalServerError)
            
            if (success) call.respond(HttpStatusCode.OK)
        }
    }
    
    route("/status") {
        post("/send") {
            val principal = call.principal<AuthResponse>()
                ?: return@post call.respond(HttpStatusCode.Unauthorized)
            
            val user = principal.user
            val db = principal.db
            
            val interaction = tryIncoming("Unable to receive interaction") {
                call
                    .receive<InteractionInfoData>()
                    .toInteractionInfo(user, db)
            } ?: return@post call.respond(HttpStatusCode.BadRequest)
            
            val success = tryInternal("Unable to insert interaction.") {
                InteractionInfoRepository(db).insertInteraction(interaction)
            } ?: return@post call.respond(HttpStatusCode.InternalServerError)
            
            if (success) call.respond(HttpStatusCode.OK)
        }
        
        get("/by-message-ext-id/{messageExtId}/by-user-ext-id/{userExtId}") {
            val principal = call.principal<AuthResponse>()
                ?: return@get call.respond(HttpStatusCode.Unauthorized)
            
            if (principal.user.isAnyAdmin != true)
                call.respond(HttpStatusCode.Forbidden)
            
            val db = principal.db
            
            val messageExtId = tryIncoming("Invalid message extId") {
                call.parameters["messageExtId"]
            } ?: return@get call.respond(HttpStatusCode.BadRequest)
            
            val userExtId = tryIncoming("Invalid user extId") {
                call.parameters["userExtId"]
            } ?: return@get call.respond(HttpStatusCode.BadRequest)
            
            val interactions = tryInternal("Unable to fetch statuses") {
                val messageId = MessageRepository(db)
                    .getMessageIdByExternalId(messageExtId)
                    ?: return@tryInternal null
                
                val userId = UserRepository(db)
                    .getUserIdByExternalId(userExtId)
                    ?: return@tryInternal null
                
                InteractionInfoRepository(db)
                    .getInteractionInfosByMessageAndUserId(messageId, userId)
                    .filter { it.type == InteractionType.status }
                    .distinctBy { it.status }.let { interactions ->
                        if (interactions.size > 3)
                            error("There are more than 3 status interactions in the db for messageId: $messageId and userId: $userId")
                        
                        val sentAt = try {
                            interactions
                                .single { it.status == MessageStatus.sent }
                                .createdAt.toEpochMilliseconds()
                        } catch (_: Exception) { null }
                        
                        val receivedAt = try {
                            interactions
                                .single { it.status == MessageStatus.received }
                                .createdAt.toEpochMilliseconds()
                        } catch (_: Exception) { null }
                        
                        val readAt = try {
                            interactions
                                .single { it.status == MessageStatus.read }
                                .createdAt.toEpochMilliseconds()
                        } catch (_: Exception) { null }
                        
                        return@let MessageStatusData(
                            userExtId = userExtId,
                            sentAt = sentAt,
                            receivedAt = receivedAt,
                            readAt = readAt,
                        )
                    }
            } ?: return@get call.respond(HttpStatusCode.InternalServerError)
            
            call.respond(interactions)
        }
    }
}