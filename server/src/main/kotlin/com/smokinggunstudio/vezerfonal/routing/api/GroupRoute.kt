package com.smokinggunstudio.vezerfonal.routing.api

import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.toGroup
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.MembershipRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Route.groupRoute() {
    get("/data") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val user = principal.user
        val db = principal.db
        
        val groups = tryInternal("Unable to get groups by member userId.") {
            GroupRepository(db).let {
                if (user.isSuperAdmin) it.getAllGroups()
                else it.getAllGroupsByMemberUserId(user.id!!)
            }.map { it.toDTO() }
        } ?: return@get
        
        call.respond(groups)
    }
    
    get("/im-admin-of") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val adminId = principal.user.id!!
        val db = principal.db
        
        val groups = tryInternal("Unable to get groups.") {
            GroupRepository(db)
                .getGroupsByAdminId(adminId)
                .map { it.toDTO() }
        } ?: return@get
        
        call.respond(groups)
    }
    
    post("/create") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        if (!principal.user.isSuperAdmin)
            call.respond(HttpStatusCode.Unauthorized)
        
        val db = principal.db
        
        val group = tryIncoming("Unable to receive group data.")
        { call.receive<GroupData>().toGroup(db) } ?: return@post
        
        val success = tryInternal("Unable to insert group.")
        { GroupRepository(db).insertGroup(group) } ?: return@post
        
        if (success) call.respond(HttpStatusCode.OK)
    }
    
    post("/join") {
        val principal = call.principal<AuthResponse>()
            ?: return@post call.respond(HttpStatusCode.Unauthorized)
        
        val userId = principal.user.id!!
        val db = principal.db
        
        val groupExtId = tryIncoming("Unable to receive extId.") {
            call.receive<String>()
        } ?: return@post
        
        val group = tryInternal("Unable to find group with ext id: $groupExtId")
        { GroupRepository(db).getGroupByExtId(groupExtId) } ?: return@post
        
        val success = tryInternal("Unable to join group: ${group.displayName}.") {
            MembershipRepository(db)
                .insertMemberIntoGroup(
                    newUserId = userId,
                    newGroupId = group.id!!
                )
        } ?: return@post println("Unable to join group: ${group.displayName}.")
        
        if (success) call.respond(group.toDTO())
    }
    
    get("/extId") {
        val principal = call.principal<AuthResponse>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)
        
        val extId = tryIncoming("Unable to receive extId.") {
            call.receive<String>()
        } ?: return@get
        
        val db = principal.db
        val userId = principal.user.id!!
        
        val group = tryInternal("Unable to get group by ext id: $extId") {
            val grepo = GroupRepository(db)
            val g = grepo
                .getGroupByExtId(extId)
            val ugs = grepo
                .getAllGroupsByMemberUserId(userId)
            if (g != null && g in ugs) g
            else null
        } ?: return@get
        
        call.respond(group)
    }
}