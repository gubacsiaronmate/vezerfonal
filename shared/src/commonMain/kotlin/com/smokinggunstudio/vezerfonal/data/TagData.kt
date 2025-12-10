package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class TagData(
    override val name: String
) : NamedDTO, DTO {
    override fun toSerializable(): Map<String, Any?> {
        return mapOf("name" to name)
    }
}
