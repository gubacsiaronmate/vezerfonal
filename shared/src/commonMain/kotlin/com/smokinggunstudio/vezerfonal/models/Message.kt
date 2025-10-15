package com.smokinggunstudio.vezerfonal.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val user: User?,
    val group: Group?,
    val title: String,
    val content: String,
    val isUrgent: Boolean,
    val author: User,
    val tags: List<Tag>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)
