package com.smokinggunstudio.vezerfonal.ui.state.model

import com.smokinggunstudio.vezerfonal.helpers.ExternalId

data class WriteMessageStateModel(
    val title: String = "",
    val content: String = "",
    val isUrgent: Boolean = false,
    val tags: List<String> = emptyList(),
    val status: String = "",
    val userIdentifiers: List<String> = emptyList(),
    val availableReactions: Array<String?> = Array(8) { null },
    val groups: List<ExternalId> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        
        other as WriteMessageStateModel
        
        if (isUrgent != other.isUrgent) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (tags != other.tags) return false
        if (status != other.status) return false
        if (userIdentifiers != other.userIdentifiers) return false
        if (!availableReactions.contentEquals(other.availableReactions)) return false
        if (groups != other.groups) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = isUrgent.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + userIdentifiers.hashCode()
        result = 31 * result + availableReactions.contentHashCode()
        result = 31 * result + groups.hashCode()
        return result
    }
}
