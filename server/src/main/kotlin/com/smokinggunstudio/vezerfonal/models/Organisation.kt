package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.OrgData
import kotlinx.datetime.LocalDateTime
import java.util.UUID


data class Organisation(
    val id: Int?,
    val name: String,
    val externalId: String,
    val createdAt: LocalDateTime
) {
    fun toDTO() = OrgData(
        externalId = externalId,
        name = name
    )
}