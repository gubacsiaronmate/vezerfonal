package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UserInteractionData(
    val user: UserData,
    val interaction: InteractionInfoData,
) : DTO {
    override fun toSerialized(): String {
        return Json.encodeToString(this)
    }
}
