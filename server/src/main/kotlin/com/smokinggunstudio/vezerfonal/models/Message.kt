package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Message @OptIn(ExperimentalTime::class) constructor(
    val id: Int?,
    val user: User?,
    val author: User,
    val group: Group?,
    val title: String,
    val content: String,
    val tags: List<Tag>,
    val isUrgent: Boolean,
    val externalId: String,
    val status: MessageStatus,
    val availableReactions: List<String>?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val deletedAt: Instant?
) {
    init {
        require((user == null) != (group == null)) { "Only one can and must be null." }
    }
    
    @OptIn(ExperimentalTime::class)
    fun toDTO(reactedWith: String?): MessageData = MessageData(
        title = title,
        author = author.toDTO(),
        content = content,
        isUrgent = isUrgent,
        tags = tags.map { tag -> tag.name },
        status = status,
        userIdentifiers = null,
        availableReactions = availableReactions,
        groups = null,
        sentAt = (createdAt ?: Clock.System.now()).toEpochMilliseconds(),
        reactedWith = reactedWith,
        externalId = externalId,
    )
}
