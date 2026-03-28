package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class TwoFactorLoginRequest(
    val email: String,
    val orgExternalId: String,
    val code: Int,
    val rememberMe: Boolean,
)
