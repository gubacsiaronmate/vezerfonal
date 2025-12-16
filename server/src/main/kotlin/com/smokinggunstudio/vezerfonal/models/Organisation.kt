package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.OrgData
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class Organisation @OptIn(ExperimentalTime::class) constructor(
    val id: Int?,
    val name: String,
    val externalId: String,
    val createdAt: Instant
) {
    fun toDTO() = OrgData(
        externalId = externalId,
        name = name
    )
}