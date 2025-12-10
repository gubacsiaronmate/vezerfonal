package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class GroupData(
    override val name: String,
    val externalId: ExternalId,
    val description: String,
    val members: List<Identifier>,
    val adminIdentifier: Identifier
) : NamedDTO, DTO {
    override fun toSerializable(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "externalId" to externalId,
            "description" to description,
            "members" to members,
            "adminIdentifier" to adminIdentifier
        )
    }
}
