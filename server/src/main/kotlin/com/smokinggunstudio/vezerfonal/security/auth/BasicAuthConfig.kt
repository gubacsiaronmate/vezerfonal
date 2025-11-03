package com.smokinggunstudio.vezerfonal.security.auth

import com.smokinggunstudio.vezerfonal.helpers.AuthResponse
import com.smokinggunstudio.vezerfonal.repositories.getUserByEmail
import com.smokinggunstudio.vezerfonal.security.verifyPassword
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.basic
import kotlin.coroutines.CoroutineContext

fun configureBasicAuth(feature: AuthenticationConfig, context: CoroutineContext) {
    feature.basic("basic") {
        validate { credentials ->
            val user = getUserByEmail(credentials.name, context)
                ?: return@validate AuthResponse(null)
            if (verifyPassword(credentials.password, user.password))
                AuthResponse(user.id)
            else AuthResponse(null)
        }
    }
}