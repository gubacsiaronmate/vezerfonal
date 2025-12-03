package com.smokinggunstudio.vezerfonal.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.smokinggunstudio.vezerfonal.models.JWTModel
import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.repositories.JWTRepository
import com.smokinggunstudio.vezerfonal.repositories.OrganisationRepository
import com.smokinggunstudio.vezerfonal.repositories.UserRepository
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.name
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinInstant

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
        .acceptLeeway(365L * 24 * 60 * 60)
        .build()
    
    @OptIn(ExperimentalTime::class)
    suspend fun generateToken(userId: Int, context: CoroutineContext, db: Database, mainDB: Database, isRefresh: Boolean = false): String = withContext(context) {
        val tokenId = UUID.randomUUID().toString()
        val expiresAt = when (isRefresh) {
            false -> Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_IN_MS)
            true -> Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_IN_MS)
        }
        
        val orgName = (transaction { db.config.defaultSchema!!.identifier }).removePrefix("vezerfonal_org_")
        val orgId = OrganisationRepository(mainDB).getOrganisationByName(orgName)?.id
            ?: error("Cannot resolve organisation by name: $orgName")
        
        val jwt = JWT.create()
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("tokenId", tokenId)
            .withClaim("orgId", orgId)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
        
        val success = JWTRepository(db).insertJWT(
            JWTModel(
                id = tokenId,
                tokenHash = hashLongString(jwt),
                isRefresh = isRefresh,
                user = UserRepository(db).getUserById(userId)!!,
                revoked = false,
                expiresAt = expiresAt.toInstant().toKotlinInstant().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
        
        return@withContext if (success) jwt else throw IllegalArgumentException("Unable to insert token into database.")
    }
}