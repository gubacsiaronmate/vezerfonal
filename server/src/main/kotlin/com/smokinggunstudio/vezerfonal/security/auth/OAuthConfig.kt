package com.smokinggunstudio.vezerfonal.security.auth

import io.ktor.server.auth.*
import kotlin.coroutines.CoroutineContext

// IGNORE: This feature requires developer accounts which will not be created for these reasons:
// Apple: it requires payment
// Google: they want to close Android which I will never support
fun CoroutineContext.configureOAuth(feature: AuthenticationConfig) {
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
    
    // Apple Sign In requires a dynamically-generated JWT as its client secret.
    // The JWT must be signed with an Apple-issued EC private key (.p8 file) from
    // the Apple Developer Portal. Required values to fill in:
    //   clientId     → Apple Services ID  (e.g. com.smokinggunstudio.vezerfonal.signin)
    //   clientSecret → short-lived JWT signed with the .p8 key (regenerate before expiry; max 6 months)
    //                  generate with: https://developer.apple.com/documentation/accountorganizationaldatasharing/creating-a-client-secret
    feature.oauth("oauth-apple") {
        urlProvider = { "https://api.vezerfonal.org/register/oauth/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "apple",
                authorizeUrl = "https://appleid.apple.com/auth/authorize",
                accessTokenUrl = "https://appleid.apple.com/auth/token",
                clientId = "",      // Apple Services ID
                clientSecret = "",  // JWT signed with Apple .p8 private key
                defaultScopes = listOf("name", "email"),
            )
        }
    }
}