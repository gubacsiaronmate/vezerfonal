package com.smokinggunstudio.vezerfonal.helpers.security

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse

actual class TokenStorage {
    private var accessToken: String? = null
    
    actual suspend fun saveTokens(tokens: TokenResponse) {
        accessToken = tokens.accessToken
    }
    
    actual suspend fun getTokens(): TokenResponse? {
        return if (accessToken != null)
            TokenResponse(
                accessToken!!, null
            ) else null
    }
    
    actual suspend fun clearTokens(): Boolean {
        accessToken = null
        return true
    }
}