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
) : DTO
