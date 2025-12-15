package com.smokinggunstudio.vezerfonal.network.api

import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

suspend fun getAccessToken(tokenStorage: TokenStorage, client: HttpClient): String {
    val savedTokens = tokenStorage.getTokens()
        ?: throw UnauthorizedException()
    
    val isAccessTokenValid = checkAccessTokenValidity(savedTokens.accessToken, client)
    
    if(isAccessTokenValid)
        return savedTokens.accessToken
    
    if (savedTokens.refreshToken == null)
        throw UnauthorizedException()
    
    val tokens = refreshTokens(savedTokens.refreshToken!!, client)
    tokenStorage.saveTokens(tokens)
    return tokens.accessToken
}