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

fun configureJWTAuth(feature: AuthenticationConfig, mainDB: Database, context: CoroutineContext) {
    feature.jwt("jwt-access") {
        verifier(JWTConfig.verifier)
        validate { credentials ->
            val token = request
                .headers["Authorization"]
                ?.removePrefix("Bearer ")
                ?.trim()
                ?: return@validate null
            val userId = credentials.payload.getClaim("userId").asInt()
            val tokenId = credentials.payload.getClaim("tokenId").asString()
            val orgId = credentials.payload.getClaim("orgId").asInt()
            
            val org = OrganisationRepository(mainDB).getOrganisationById(orgId)!!
            val db = ensureOrgDB(org.name, context)
                ?: error("Cannot resolve db for org: ${org.name}")
            
            val user = UserRepository(db).getUserById(userId)
            val jrepo = JWTRepository(db)
            val jwt = jrepo.getJWTById(tokenId)
            when {
                user == null -> return@validate null
                jwt == null -> return@validate null
                jwt.revoked -> return@validate null
                jwt.isRefresh -> return@validate null
                jwt.expiresAt.isExpired() -> {
                    jrepo.modifyJWT(
                        tokenId = jwt.id,
                        property = JWTs.revoked,
                        newValue = true,
                    ); return@validate null
                }
                !verifyLongStringHash(token, jwt.tokenHash) -> return@validate null
                else -> return@validate AuthResponse(user.id!!)
            }
        }
    }
    
    feature.jwt("jwt-refresh") {
        verifier(JWTConfig.verifier)
        validate { credentials ->
            val token = request
                .headers["Authorization"]
                ?.removePrefix("Bearer ")
                ?.trim()
                ?: return@validate null
            val userId = credentials.payload.getClaim("userId").asInt()
            val tokenId = credentials.payload.getClaim("tokenId").asString()
            val orgId = credentials.payload.getClaim("orgId").asInt()
            
            val org = OrganisationRepository(mainDB).getOrganisationById(orgId)!!
            val db = ensureOrgDB(org.name, context)
                ?: error("Cannot resolve db for org: ${org.name}")
            
            val user = UserRepository(db).getUserById(userId)
            val jrepo = JWTRepository(db)
            val jwt = jrepo.getJWTById(tokenId)
            when {
                user == null -> return@validate null
                jwt == null -> return@validate null
                jwt.revoked -> return@validate null
                !jwt.isRefresh -> return@validate null
                jwt.expiresAt.isExpired() -> {
                    jrepo.modifyJWT(
                        tokenId = jwt.id,
                        property = JWTs.revoked,
                        newValue = true,
                    )
                    return@validate null
                }
                !verifyLongStringHash(token, jwt.tokenHash) -> return@validate null
                else -> return@validate AuthResponse(user.id!!)
            }
        }
    }
}
