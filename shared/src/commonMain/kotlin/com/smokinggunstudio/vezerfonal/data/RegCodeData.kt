package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class RegCodeData(
    val code: String,
    val totalUses: Int,
    val remainingUses: Int
)
