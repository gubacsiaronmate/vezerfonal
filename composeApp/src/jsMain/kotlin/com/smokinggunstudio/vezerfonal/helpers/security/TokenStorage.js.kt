package com.smokinggunstudio.vezerfonal.helpers.security

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import kotlinx.browser.window

// sessionStorage is intentionally chosen over localStorage:
//   - survives page refresh within the same tab
//   - automatically cleared when the tab/window closes (no long-lived token on disk)
//   - not shared across tabs (limits blast radius of XSS)
//   - same-origin isolated by the browser
// localStorage would persist tokens indefinitely across browser restarts — unacceptable for auth tokens.
actual class TokenStorage {
    actual suspend fun saveTokens(tokens: TokenResponse) {
        window.sessionStorage.setItem("access_token", tokens.accessToken)
        tokens.refreshToken?.let { window.sessionStorage.setItem("refresh_token", it) }
    }

    actual suspend fun getTokens(): TokenResponse? {
        val access = window.sessionStorage.getItem("access_token") ?: return null
        val refresh = window.sessionStorage.getItem("refresh_token")
        return TokenResponse(access, refresh)
    }

    actual suspend fun clearTokens(): Boolean {
        window.sessionStorage.removeItem("access_token")
        window.sessionStorage.removeItem("refresh_token")
        return true
    }
}
