package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val title: String,
    val sentAt: String,
    val content: String,
    val author: UserData,
    val isUrgent: Boolean,
    val tags: List<String>,
    val externalId: String,
    val reactedWith: String?,
    val status: MessageStatus?,
    val groups: List<ExternalId>?,
    val userIdentifiers: List<String>?,
    val availableReactions: List<String>?,
) : DTO {
    override fun toSerializable(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "sentAt" to sentAt,
            "content" to content,
            "author" to author.toSerializable(),
            "isUrgent" to isUrgent,
            "tags" to tags,
            "externalId" to externalId,
            "reactedWith" to reactedWith,
            "status" to status,
            "groups" to groups,
            "userIdentifiers" to userIdentifiers,
            "availableReactions" to availableReactions,
        )
    }
    
    init {
        require(availableReactions == null || availableReactions.size < 9)
    }
}
