package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UserData(
    val registrationCode: String?,
    val email: String,
    val password: String?,
    override val name: String,
    override val externalId: String,
    val isAnyAdmin: Boolean,
    val isSuperAdmin: Boolean,
) : NamedDTO, DTO {
    override fun toSerialized(): String {
        return Json.encodeToString(this)
    }
}