package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Membership(
    val user: User,
    val joinedAt: LocalDateTime
)
