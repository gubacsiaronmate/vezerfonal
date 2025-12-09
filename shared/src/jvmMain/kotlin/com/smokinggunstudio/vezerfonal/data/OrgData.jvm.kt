package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
actual data class OrgData actual constructor(
    actual override val name: String,
    actual val externalId: String
) : DTO