package com.smokinggunstudio.vezerfonal.models

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class JWTModel @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val tokenHash: String,
    val isRefresh: Boolean,
    val user: User,
    val revoked: Boolean,
    val createdAt: Instant? = null,
    val expiresAt: Instant
)
