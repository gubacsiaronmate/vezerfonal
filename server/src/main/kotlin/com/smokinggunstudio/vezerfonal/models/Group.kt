package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.GroupData
import kotlinx.datetime.LocalDateTime

data class Group(
    val id: Int?,
    val displayName: String,
    val description: String,
    val members: List<Membership>,
    val admin: User,
    val isInternal: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    fun toDTO(): GroupData = GroupData(
        name = displayName,
        description = description,
        members = members.map { it.user.identifier },
        adminIdentifier = admin.identifier
    )
}
