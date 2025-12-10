package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val registrationCode: String?,
    val email: String,
    val password: String?,
    override val name: String,
    val identifier: String,
    val isAnyAdmin: Boolean,
    val isSuperAdmin: Boolean,
) : NamedDTO, DTO {
    override fun toSerializable(): Map<String, Any?> {
        return mapOf(
            "registrationCode" to registrationCode,
            "email" to email,
            "password" to password,
            "isAnyAdmin" to isAnyAdmin,
            "isSuperAdmin" to isSuperAdmin,
        )
    }
}