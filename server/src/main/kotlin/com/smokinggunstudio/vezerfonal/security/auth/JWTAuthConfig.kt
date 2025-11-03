package com.smokinggunstudio.vezerfonal.security.auth

import com.auth0.jwt.JWT
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt
import kotlin.coroutines.CoroutineContext

fun configureJWTAuth(feature: AuthenticationConfig, context: CoroutineContext) {
    feature.jwt("jwt") {
        verifier(JWTConfig.verifier)
        validate { credentials ->
            val userId = credentials.payload.getClaim("userId").asInt()
            
        }
    }
}