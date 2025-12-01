package com.smokinggunstudio.vezerfonal.models

data class RegistrationCode(
    val id: Int?,
    val code: String,
    val totalUses: Int,
    val remainingUses: Int,
    val organisation: Organisation
)