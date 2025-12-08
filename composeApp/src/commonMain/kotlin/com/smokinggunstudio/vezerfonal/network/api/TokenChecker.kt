package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.UnauthorizedException
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
            val tokens = try {
                refreshTokens(savedTokens.refreshToken!!, client)
            } catch (e: UnauthorizedException) {
                throw e
            }
            tokenStorage.saveTokens(tokens)
            tokens.accessToken
        }
    }
}