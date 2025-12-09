package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
expect class OrgData: DTO {
    override val name: String
    val externalId: String
}
