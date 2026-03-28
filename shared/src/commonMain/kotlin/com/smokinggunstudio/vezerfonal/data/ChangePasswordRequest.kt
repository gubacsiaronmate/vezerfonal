package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val code: Int,
    val newPassword: String,
)
