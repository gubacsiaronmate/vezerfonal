package com.smokinggunstudio.vezerfonal.helpers.security

import android.content.Context
import com.liftric.kvault.KVault
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse

actual class TokenStorage actual constructor() {
    private lateinit var kVault: KVault
    
    constructor(context: Context) : this() {
        this.kVault = KVault(context)
    }
    
    actual suspend fun saveTokens(tokens: TokenResponse) {
        kVault.set("access_token", tokens.accessToken)
        tokens.refreshToken?.let { kVault.set("refresh_token", it) }
    }
    
    actual suspend fun getTokens(): TokenResponse? {
        val access = kVault.string("access_token")
        val refresh = kVault.string("refresh_token")
        return if (access != null && refresh != null)
            TokenResponse(access, refresh)
        else null
    }
    
    actual suspend fun clearTokens(): Boolean = kVault.clear()
}