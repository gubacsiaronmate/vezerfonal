package com.smokinggunstudio.vezerfonal.security.auth

import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.oauth
import kotlin.coroutines.CoroutineContext

fun configureOAuth(feature: AuthenticationConfig, context: CoroutineContext) {
    feature.oauth("oauth-google") {
        urlProvider = { "https://api.vezerfonal.org/register/oauth/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "google",
                authorizeUrl = "https://accounts.google.com/o/oauth2/v2/auth",
                accessTokenUrl = "https://oauth2.googleapis.com/token",
                clientId = "",
                clientSecret = "",
                defaultScopes = listOf("")
            )
        }
    }
    
    feature.oauth("oauth-apple") {
        // TODO
    }
}