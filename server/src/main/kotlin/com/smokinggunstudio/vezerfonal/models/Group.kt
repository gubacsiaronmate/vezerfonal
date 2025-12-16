package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.GroupData
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Group @OptIn(ExperimentalTime::class) constructor(
    val id: Int?,
    val displayName: String,
    val description: String,
    val members: List<Membership>,
    val admin: User,
    val externalId: String,
    val isInternal: Boolean,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val deletedAt: Instant?
) {
    fun toDTO(): GroupData = GroupData(
        name = displayName,
        externalId = externalId,
        description = description,
        members = members.map { it.user.identifier },
        adminIdentifier = admin.identifier
    )
}
