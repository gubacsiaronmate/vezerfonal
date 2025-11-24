package com.smokinggunstudio.vezerfonal.helpers.security

import android.os.Build
import android.security.KeyStoreException
import androidx.annotation.RequiresApi
import com.liftric.kvault.KVault
import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import kotlin.time.Clock

actual class TokenStorage actual constructor() {
    private val context = CurrentContextProvider.current
        ?: error("Context cannot be null.")
    
    private fun createKVault(alias: String) = KVault(context, alias)
    
    private var kVault = createKVault("default")
    
    actual suspend fun saveTokens(tokens: TokenResponse) {
        kVault.set("access_token", tokens.accessToken)
        tokens.refreshToken?.let { kVault.set("refresh_token", it) }
    }
    
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual suspend fun getTokens(): TokenResponse? {
        return try {
            val access = kVault.string("access_token") ?: return null
            val refresh = kVault.string("refresh_token") ?: return null
            TokenResponse(access, refresh)
        } catch (_: KeyStoreException) {
            kVault = createKVault(System.currentTimeMillis().toString())
            clearTokens()
            null
        }
    }
    
    actual suspend fun clearTokens(): Boolean = kVault.clear()
}