package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.InteractionInfoData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.toInteractionInfo
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.InteractionInfoRepository
import com.smokinggunstudio.vezerfonal.repositories.MessageRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import jdk.internal.joptsimple.internal.Messages.message
import java.awt.TrayIcon

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
            
            call.respond(interactions)
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