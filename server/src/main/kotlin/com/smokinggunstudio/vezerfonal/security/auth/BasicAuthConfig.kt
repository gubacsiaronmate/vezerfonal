package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.repositories.getUserByEmail
import com.smokinggunstudio.vezerfonal.security.verifyPassword
import io.ktor.server.auth.*
import io.ktor.server.request.*
import kotlin.coroutines.CoroutineContext

fun configureBasicAuth(feature: AuthenticationConfig, context: CoroutineContext) {
    feature.basic("basic") {
        validate { credentials ->
            val rememberMe = receive<Boolean>()
            
            val user = getUserByEmail(credentials.name)
                ?: return@validate null
            
            val isPasswordValid = verifyPassword(credentials.password, user.password)
            
            if (isPasswordValid)
                AuthResponse(user.id!!, rememberMe)
            else null
        }
    }
}