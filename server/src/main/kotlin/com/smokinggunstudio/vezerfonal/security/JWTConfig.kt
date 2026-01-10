package com.smokinggunstudio.vezerfonal.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.smokinggunstudio.vezerfonal.helpers.log
import com.smokinggunstudio.vezerfonal.helpers.toKotlinInstant
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object JWTConfig {
    private val SECRET by lazy {
        System.getenv("JWT_SECRET")
            ?: throw IllegalArgumentException("JWT signing secret key is not found")
    }
    private const val ISSUER = "https://api.vezerfonal.org"
    private const val AUDIENCE = "vezerfonal.users"
    private const val ACCESS_TOKEN_VALIDITY_IN_MS = 60 * 60 * 1000L // 1 hour
    private const val REFRESH_TOKEN_VALIDITY_IN_MS = 30 * 24 * 60 * 60 * 1000L // 30 days
    
    private val algorithm = Algorithm.HMAC256(SECRET)
    
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
//        .acceptLeeway(365L * 24 * 60 * 60)
        .build()
    
    @OptIn(ExperimentalTime::class)
    suspend fun generateToken(
        userExtId: String,
        db: Database,
        mainDB: Database,
        isRefresh: Boolean = false
    ): String {
        val tokenId = UUID.randomUUID().toString()
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val expiresAt = when (isRefresh) {
            false -> Date(currentTime + ACCESS_TOKEN_VALIDITY_IN_MS)
            true -> Date(currentTime + REFRESH_TOKEN_VALIDITY_IN_MS)
        }
        log { "expires at: $expiresAt" }
        val orgName =
            transaction {
                db.config.defaultSchema!!.identifier
            }.removePrefix("vezerfonal_org_")
        
        val orgExtId = OrganisationRepository(mainDB)
            .getOrganisationByName(orgName)?.externalId
            ?: error("Cannot resolve organisation by name: $orgName")
        
        val jwt = JWT.create()
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userExtId", userExtId)
            .withClaim("tokenId", tokenId)
            .withClaim("orgExtId", orgExtId)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
        
        val user = UserRepository(db)
            .getUserByExternalId(userExtId)
            ?: error("Cannot resolve user by id: $userExtId")
        
        val success = with(JWTRepository(db)) {
            val insertSuccess = insertJWT(
                JWTModel(
                    id = tokenId,
                    tokenHash = hashLongString(jwt),
                    isRefresh = isRefresh,
                    user = user,
                    revoked = false,
                    expiresAt = expiresAt.toKotlinInstant()
                )
            )
            
            return@with if (insertSuccess) true
            else invalidateAllTokensByUserId(user.id!!)
        }
        
        return if (success) jwt
        else error("Unable to insert token into database.")
    }
}