package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class OrgData(
    override val name: String,
    val externalId: String
) : NamedDTO, DTO {
    override fun toSerializable(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "externalId" to externalId
        )
    }
}