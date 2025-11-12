package com.smokinggunstudio.vezerfonal.helpers.security

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse

expect class TokenStorage() {
    suspend fun saveTokens(tokens: TokenResponse)
    suspend fun getTokens(): TokenResponse?
    suspend fun clearTokens(): Boolean
}