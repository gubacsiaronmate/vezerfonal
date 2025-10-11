package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime

data class Devices(
    val id: Int,
    val user: User,
    val pushToken: String,
    val session: Session,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime
)
