package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.RegCodeData

data class RegistrationCode(
    val id: Int?,
    val code: String,
    val totalUses: Int,
    val remainingUses: Int,
    val organisation: Organisation
) {
    fun toDTO() = RegCodeData(
        code = code,
        totalUses = totalUses,
        remainingUses = remainingUses
    )
}