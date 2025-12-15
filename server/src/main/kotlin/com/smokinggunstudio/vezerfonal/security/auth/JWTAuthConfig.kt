package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.isExpired
import com.smokinggunstudio.vezerfonal.objects.JWTs
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.verifyLongStringHash
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.coroutines.CoroutineContext

fun AuthenticationConfig.configureJWTAuth(mainDB: Database) {
    jwt("jwt-access") {
        verifier(JWTConfig.verifier)
        validate { credentials ->
            val token = request
                .headers["Authorization"]
                ?.removePrefix("Bearer ")
                ?.trim()
                ?: return@validate null
            
            val userExtId = credentials.payload.getClaim("userExtId").asString()
            val tokenId = credentials.payload.getClaim("tokenId").asString()
            val orgExtId = credentials.payload.getClaim("orgExtId").asString()
            
            val org = OrganisationRepository(mainDB)
                .getOrganisationByExternalId(orgExtId)
                ?: return@validate null
            
            val db = ensureOrgDB(org.name)
                ?: return@validate null
            
            val user = UserRepository(db)
                .getUserByIdentifier(userExtId)
                ?: return@validate null
            
            val jrepo = JWTRepository(db)
            val jwt = jrepo.getJWTById(tokenId)
                ?: return@validate null
            
            if (jwt.expiresAt.isExpired())
                jrepo.modifyJWT(jwt.id, JWTs.revoked, true)
            
            return@validate when {
                jwt.revoked -> null
                jwt.isRefresh -> null
                !verifyLongStringHash(token, jwt.tokenHash) -> null
                else -> AuthResponse(user, db, org)
            }
        }
    }
    
    jwt("jwt-refresh") {
        verifier(JWTConfig.verifier)
        validate { credentials ->
            val token = request
                .headers["Authorization"]
                ?.removePrefix("Bearer ")
                ?.trim()
                ?: return@validate null
            
            val userExtId = credentials.payload.getClaim("userExtId").asString()
            val tokenId = credentials.payload.getClaim("tokenId").asString()
            val orgExtId = credentials.payload.getClaim("orgExtId").asString()
            
            val org = OrganisationRepository(mainDB)
                .getOrganisationByExternalId(orgExtId)
                ?: return@validate null
            
            val db = ensureOrgDB(org.name)
                ?: return@validate null
            
            val user = UserRepository(db)
                .getUserByIdentifier(userExtId)
                ?: return@validate null
            
            val jrepo = JWTRepository(db)
            val jwt = jrepo.getJWTById(tokenId)
                ?: return@validate null
            
            if (jwt.expiresAt.isExpired())
                jrepo.modifyJWT(jwt.id, JWTs.revoked, true)
            
            return@validate when {
                jwt.revoked -> null
                !jwt.isRefresh -> null
                !verifyLongStringHash(token, jwt.tokenHash) -> null
                else -> AuthResponse(user, db, org)
            }
        }
    }
}
