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
            println("Credentials.name (email): ${credentials.name}")
            println("Credentials.password: ${credentials.password}")
            
            val rememberMe = receive<Boolean>()
            println("Remember me: $rememberMe")
            
            val user = getUserByEmail(credentials.name, context)
                ?: return@validate null
            println("User.email: ${user.email}")
            println("User.password: ${user.password}")
            
            val isPasswordValid = verifyPassword(credentials.password, user.password)
            println("IsPasswordValid: $isPasswordValid")
            
            if (isPasswordValid)
                AuthResponse(user.id!!, rememberMe)
            else null
        }
    }
}