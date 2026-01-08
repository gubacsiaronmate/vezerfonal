package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class OrgData(
    override val name: String,
    override val externalId: String
) : NamedDTO, DTO