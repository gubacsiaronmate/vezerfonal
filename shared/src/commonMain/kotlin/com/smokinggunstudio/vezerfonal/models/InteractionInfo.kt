package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class InteractionInfo(
    val id: Int,
    val message: Message,
    val user: User,
    val type: InteractionType?,
    val status: MessageStatus?,
    val reaction: String?,
    val actor: User?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
)