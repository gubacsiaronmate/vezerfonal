package com.smokinggunstudio.vezerfonal.network.helpers

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.checkAccessTokenValidity
import com.smokinggunstudio.vezerfonal.network.api.refreshTokens
import io.ktor.client.*

suspend fun getAccessToken(client: HttpClient): String {
    val tokenStorage = TokenStorage()
    val savedTokens = tokenStorage.getTokens()
        ?: throw UnauthorizedException("Log in please")
    
    val isAccessTokenValid = checkAccessTokenValidity(savedTokens.accessToken, client)
    
    return if(isAccessTokenValid) savedTokens.accessToken
    else when(savedTokens.refreshToken) {
        null -> throw UnauthorizedException("Log in please")
        else -> {
            val tokens = refreshTokens(savedTokens.refreshToken!!, client)
            tokenStorage.saveTokens(tokens)
            tokens.accessToken
        }
    }
}