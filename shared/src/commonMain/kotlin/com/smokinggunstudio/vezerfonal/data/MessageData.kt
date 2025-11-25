package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val title: String,
    val author: String,
    val content: String,
    val isUrgent: Boolean,
    val tags: List<String>,
    val status: MessageStatus?,
    val userIdentifiers: List<String>?,
    val availableReactions: List<String>?,
    val groups: List<GroupData>?
) {
    init {
        require(availableReactions == null || availableReactions.size < 9)
    }
}
