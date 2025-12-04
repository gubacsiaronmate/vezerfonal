package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val title: String,
    val author: Identifier,
    val content: String,
    val isUrgent: Boolean,
    val tags: List<String>,
    val status: MessageStatus?,
    val userIdentifiers: List<Identifier>?,
    val availableReactions: List<String>?,
    val groups: List<ExternalId>?
) {
    init {
        require(availableReactions == null || availableReactions.size < 9)
    }
}
