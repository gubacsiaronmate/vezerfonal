package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.datetime.LocalDateTime

data class Message(
    val id: Int?,
    val user: User?,
    val group: Group?,
    val title: String,
    val content: String,
    val isUrgent: Boolean,
    val author: User,
    val availableReactions: List<String>?,
    val status: MessageStatus?,
    val tags: List<Tag>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    init {
        require((user == null) != (group == null)) { "Only one can and must be null." }
    }
    
    fun toDTO(): MessageData = MessageData(
        title = title,
        author = author.displayName,
        content = content,
        isUrgent = isUrgent,
        tags = tags.map { tag -> tag.tagName },
        status = status,
        userIdentifiers = null,
        availableReactions = availableReactions,
        groudAdminIdentifiers = null
    )
}
