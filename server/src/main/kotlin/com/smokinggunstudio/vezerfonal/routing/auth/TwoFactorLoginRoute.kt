package com.smokinggunstudio.vezerfonal.routing.auth

import com.smokinggunstudio.vezerfonal.data.TwoFactorLoginRequest
import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import com.smokinggunstudio.vezerfonal.helpers.tryIncoming
import com.smokinggunstudio.vezerfonal.helpers.tryInternal
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.hashLongString
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.jetbrains.exposed.v1.jdbc.Database

fun Route.twoFactorLoginRoute(mainDB: Database) {
    post("/2fa") {
        val request = tryIncoming("Unable to receive 2FA request.") {
            call.receive<TwoFactorLoginRequest>()
        } ?: return@post call.respond(HttpStatusCode.BadRequest)

        val org = tryInternal("Unable to resolve organisation.") {
            OrganisationRepository(mainDB).getOrganisationByExternalId(request.orgExternalId)
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val db = ensureOrgDB(org.name)
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val repo = UserRepository(db)

        val user = tryInternal("Unable to find user.") {
            repo.getUserByEmail(request.email)
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)

        val storedHash = tryInternal("Unable to retrieve 2FA code.") {
            repo.getTwoFactorCodeHash(user.id!!)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        if (storedHash != hashLongString(request.code.toString()))
            return@post call.respond(HttpStatusCode.Forbidden)

        tryInternal("Unable to clear 2FA code.") {
            repo.setTwoFactorCode(user.id!!, null)
        } ?: return@post call.respond(HttpStatusCode.InternalServerError)

        val success = tryInternal("Unable to invalidate old tokens.") {
            JWTRepository(db).invalidateAllTokensByUserId(user.id!!)
        } ?: return@post call.respond(HttpStatusCode.Unauthorized)

        if (!success) return@post call.respond(HttpStatusCode.InternalServerError)

        val accessToken = tryInternal("Cannot generate jwt.") {
            JWTConfig.generateToken(userExtId = user.externalId, db = db, mainDB = mainDB)
        } ?: return@post

        val refreshToken = if (request.rememberMe)
            tryInternal("Cannot generate jwt.") {
                JWTConfig.generateToken(
                    userExtId = user.externalId,
                    db = db,
                    mainDB = mainDB,
                    isRefresh = true
                )
            }
        else null

        call.respond(TokenResponse(accessToken, refreshToken))
    }
}
