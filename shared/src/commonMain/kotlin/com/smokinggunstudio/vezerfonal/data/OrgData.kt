package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class OrgData(
    val externalId: String,
    val name: String
)
