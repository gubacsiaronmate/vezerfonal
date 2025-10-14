package com.smokinggunstudio.vezerfonal.models

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationCode(
    val id: Int,
    val code: String,
    val totalUses: Int,
    val remainingUses: Int
)