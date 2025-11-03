package com.smokinggunstudio.vezerfonal.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

object JWTConfig {
    private const val SECRET = "super-secret-key"
    private const val ISSUER = "https://api.vezerfonal.org"
    private const val AUDIENCE = "vezerfonal.users"
    private const val VALIDITY_IN_MS = 1000L * 60L * 60L // 1 hour
    
    private val algorithm = Algorithm.HMAC256(SECRET)
    
    val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()
    
    suspend fun generateToken(userId: String, context: CoroutineContext): String = withContext(context) {
        val tokenId = UUID.randomUUID().toString()
        
        JWT.create()
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("tokenId", tokenId)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_IN_MS))
            .sign(algorithm)
    }
}