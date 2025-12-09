package com.smokinggunstudio.vezerfonal.data

import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val title: String,
    val author: UserData,
    val content: String,
    val isUrgent: Boolean,
    val tags: List<String>,
    val status: MessageStatus?,
    val userIdentifiers: List<String>?,
    val availableReactions: List<String>?,
    val groups: List<ExternalId>?,
    val sentAt: String,
    val reactedWith: String?
// ha empty str ("") akkor default react ha null akkor meg nincs reaction ha van X emoji benne akkor az a react
) {
    init {
        require(availableReactions == null || availableReactions.size < 9)
    }
}
