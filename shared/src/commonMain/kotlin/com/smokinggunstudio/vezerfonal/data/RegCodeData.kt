package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class RegCodeData(
    val code: String,
    val totalUses: Int,
    val remainingUses: Int
) : DTO {
    override fun toSerializable(): Map<String, Any?> {
        return mapOf(
            "code" to code,
            "totalUses" to totalUses,
            "remainingUses" to remainingUses
        )
    }
}
