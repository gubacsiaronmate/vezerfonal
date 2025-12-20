package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.database.ensureOrgDB
import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.isExpired
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.objects.JWTs
import com.smokinggunstudio.vezerfonal.repositories.GroupRepository
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.verifyLongStringHash
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.name
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun AuthenticationConfig.configureJWTAuth(mainDB: Database) {
    fun JWTAuthenticationProvider.Config.configure(isRefresh: Boolean) {
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
            
            val user = with(UserRepository(db).getUserByIdentifier(userExtId)) {
                if (this == null) return@validate null
                isAnyAdmin = GroupRepository(db).getGroupsByAdminId(id!!).isNotEmpty() || isSuperAdmin
                return@with this
            }
            
            val jrepo = JWTRepository(db)
            var jwt = jrepo.getJWTById(tokenId)
                ?: return@validate null
            
            if (jwt.expiresAt.toEpochMilliseconds().isExpired())
                jrepo.modifyJWT(jwt.id, JWTs.revoked, true)
            
            jwt = jrepo.getJWTById(tokenId)
                ?: return@validate null
            
            return@validate when {
                jwt.revoked -> null
                jwt.isRefresh != isRefresh -> null
                !verifyLongStringHash(token, jwt.tokenHash) -> null
                else -> AuthResponse(user, db, org)
            }
        }
    }
    
    jwt("jwt-access") { configure(isRefresh = false) }
    
    jwt("jwt-refresh") { configure(isRefresh = true) }
}
