package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MessageData(
    val title: String,
    val sentAt: Long,
    val content: String,
    val author: UserData,
    val isUrgent: Boolean,
    val tags: List<String>,
    val externalId: String,
    val reactedWith: String?,
    val status: MessageStatus,
    val groups: List<ExternalId>?,
    val userIdentifiers: List<String>?,
    val availableReactions: List<String>?,
) : DTO {
    init {
        require(availableReactions == null || availableReactions.size < 9)
    }
}
