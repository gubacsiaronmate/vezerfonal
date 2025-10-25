package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime


data class Group(
    val id: Int?,
    val displayName: String,
    val description: String,
    val members: List<Membership>,
    val admin: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)
