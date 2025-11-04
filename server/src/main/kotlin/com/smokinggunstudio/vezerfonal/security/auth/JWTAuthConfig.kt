package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.helpers.isExpired
import com.smokinggunstudio.vezerfonal.objects.JWTs
import com.smokinggunstudio.vezerfonal.repositories.getJWTById
import com.smokinggunstudio.vezerfonal.repositories.getUserById
import com.smokinggunstudio.vezerfonal.repositories.modifyJWT
import com.smokinggunstudio.vezerfonal.security.JWTConfig
import com.smokinggunstudio.vezerfonal.security.verifyLongStringHash
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlin.coroutines.CoroutineContext

fun configureJWTAuth(feature: AuthenticationConfig, context: CoroutineContext) {
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
            val user = getUserById(userId, context)
            val jwt = getJWTById(tokenId, context)
            when {
                user == null -> return@validate null
                jwt == null -> return@validate null
                jwt.revoked -> return@validate null
                jwt.isRefresh -> return@validate null
                jwt.expiresAt.isExpired() -> {
                    modifyJWT(
                        tokenId = jwt.id,
                        property = JWTs.revoked,
                        newValue = true,
                        context = context
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
            val user = getUserById(userId, context)
            val jwt = getJWTById(tokenId, context)
            when {
                user == null -> return@validate null
                jwt == null -> return@validate null
                jwt.revoked -> return@validate null
                !jwt.isRefresh -> return@validate null
                jwt.expiresAt.isExpired() -> {
                    modifyJWT(
                        tokenId = jwt.id,
                        property = JWTs.revoked,
                        newValue = true,
                        context = context
                    ); return@validate null
                }
                !verifyLongStringHash(token, jwt.tokenHash) -> return@validate null
                else -> return@validate AuthResponse(user.id!!)
            }
        }
    }
}