package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegCodeData(
    val code: String,
    val totalUses: Int,
    val remainingUses: Int
) : DTO {
    override fun toSerialized(): String {
        return Json.encodeToString(this)
    }
}
