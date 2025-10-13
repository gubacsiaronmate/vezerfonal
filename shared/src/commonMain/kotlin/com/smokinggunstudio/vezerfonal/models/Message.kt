package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime

data class Message(
    val id: Int,
    val user: User?,
    val group: Group?,
    val content: String,
    val isUrgent: Boolean,
    val author: User,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)
