package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class OrgData(
    override val name: String,
    val externalId: String,
) : DTO
