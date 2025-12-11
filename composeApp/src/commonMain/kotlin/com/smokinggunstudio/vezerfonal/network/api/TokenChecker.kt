package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import io.ktor.client.*

suspend fun getAccessToken(tokenStorage: TokenStorage, client: HttpClient): String {
    val savedTokens = tokenStorage.getTokens()
        ?: throw UnauthorizedException()
    
    val isAccessTokenValid = checkAccessTokenValidity(savedTokens.accessToken, client)
    
    return if(isAccessTokenValid) savedTokens.accessToken
    else when(savedTokens.refreshToken) {
        null -> throw UnauthorizedException()
        else -> {
            val tokens = refreshTokens(savedTokens.refreshToken!!, client)
            tokenStorage.saveTokens(tokens)
            tokens.accessToken
        }
    }
}