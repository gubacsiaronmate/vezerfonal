package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val registrationCode: String,
    val email: String,
    val password: String?,
    val name: String,
    val identifier: String,
    val isSuperAdmin: Boolean,
)