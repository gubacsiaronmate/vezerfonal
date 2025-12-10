package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

data class Message(
    val id: Int?,
    val user: User?,
    val author: User,
    val group: Group?,
    val title: String,
    val content: String,
    val tags: List<Tag>,
    val isUrgent: Boolean,
    val externalId: String,
    val status: MessageStatus?,
    val availableReactions: List<String>?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    init {
        require((user == null) != (group == null)) { "Only one can and must be null." }
    }
    
    fun toDTO(reactedWith: String?): MessageData = MessageData(
        title = title,
        author = author.toDTO(),
        content = content,
        isUrgent = isUrgent,
        tags = tags.map { tag -> tag.tagName },
        status = status,
        userIdentifiers = null,
        availableReactions = availableReactions,
        groups = null,
        sentAt = (createdAt ?: LocalDateTime.now()).toString(),
        reactedWith = reactedWith,
        externalId = externalId,
    )
}
