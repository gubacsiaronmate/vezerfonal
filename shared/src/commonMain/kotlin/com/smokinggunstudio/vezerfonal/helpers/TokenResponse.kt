package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String?
) {
    override fun toString(): String = "TokenResponse(accessToken='$accessToken', refreshToken='$refreshToken')"
}