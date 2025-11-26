package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.checkAccessTokenValidity
import com.smokinggunstudio.vezerfonal.network.api.refreshTokens
import io.ktor.client.*

suspend fun getAccessToken(tokenStorage: TokenStorage, client: HttpClient): String? {
    val savedTokens = tokenStorage.getTokens()
        ?: return null
    
    val isAccessTokenValid = checkAccessTokenValidity(savedTokens.accessToken, client)
    
    return if(isAccessTokenValid) savedTokens.accessToken
    else when(savedTokens.refreshToken) {
        null -> null
        else -> {
            val tokens = refreshTokens(savedTokens.refreshToken!!, client)
                ?: return null
            tokenStorage.saveTokens(tokens)
            tokens.accessToken
        }
    }
}