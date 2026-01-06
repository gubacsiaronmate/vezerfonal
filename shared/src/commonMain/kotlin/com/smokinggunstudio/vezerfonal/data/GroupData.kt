package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GroupData(
    override val name: String,
    override val externalId: String,
    val description: String,
    val members: List<Identifier>,
    val adminIdentifier: Identifier
) : NamedDTO, DTO {
    override fun toSerialized(): String {
        return Json.encodeToString(this)
    }
}
