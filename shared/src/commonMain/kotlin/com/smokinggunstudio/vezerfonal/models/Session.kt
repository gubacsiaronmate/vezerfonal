package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: Int,
    val user: User,
    val jti: String,
    val refreshToken: String,
    val ipAddress: String?,
    val revoked: Boolean,
    val deviceInfo: String,
    val lastUsedAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)
