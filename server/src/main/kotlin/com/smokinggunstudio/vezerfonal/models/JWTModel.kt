package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime

data class JWTModel(
    val id: String,
    val tokenHash: String,
    val isRefresh: Boolean,
    val user: User,
    val revoked: Boolean,
    val createdAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime
)
