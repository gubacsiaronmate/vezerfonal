package com.smokinggunstudio.vezerfonal.routing

import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun RoutingContext.organisationRoute(mainDB: Database) {
    val orgs = OrganisationRepository(mainDB)
        .getOrganisations()
        .filter { it.id != 67 }
        .map { it.toDTO() }
    
    call.respond(orgs)
}