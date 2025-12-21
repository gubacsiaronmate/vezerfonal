package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
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
                val asd = call.parameters["messageExtId"]
                asd
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
                        .getUserByIdentifier(it.userIdentifier)
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
                InteractionInfoRepository(db)
                    .insertInteraction(interaction)
            } ?: return@post call.respond(HttpStatusCode.InternalServerError)
            
            if (success) call.respond(HttpStatusCode.OK)
        }
    }
}