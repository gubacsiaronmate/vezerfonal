package com.smokinggunstudio.vezerfonal.helpers.security

import com.liftric.kvault.KVault
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse

actual class TokenStorage {
    private val kVault = KVault()
    
    actual suspend fun saveTokens(tokens: TokenResponse) {
        kVault.set("access_token", tokens.accessToken)
        tokens.refreshToken?.let { kVault.set("refresh_token", it) }
    }
    
    actual suspend fun getTokens(): TokenResponse? {
        val access = kVault.string("access_token")
            ?: return null
        
        val refresh = kVault.string("refresh_token")
            ?: return null
        
        return TokenResponse(access, refresh)
    }
    
    actual suspend fun clearTokens(): Boolean = kVault.clear()
}