package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import kotlin.collections.emptyList

class WriteMessageState {
    private val _title = mutableStateOf("")
    val title: String get() = _title.value
    
    private val _content = mutableStateOf("")
    val content: String get() = _content.value
    
    private val _isUrgent = mutableStateOf(false)
    val isUrgent: Boolean get() = _isUrgent.value
    
    private val _tags = mutableStateOf<List<String>>(emptyList())
    val tags: List<String> get() = _tags.value
    
    private val _status = mutableStateOf("")
    val status: String get() = _status.value

    private val _userIdentifiers = mutableStateOf<List<String>>(emptyList())
    val userIdentifiers: List<String> get() = _userIdentifiers.value
    
    private val _availableReactions = mutableStateOf<List<String>>(emptyList())
    val availableReactions: List<String> get() = _availableReactions.value
    
    private val _groups = mutableStateOf<List<ExternalId>>(emptyList())
    val groups: List<ExternalId> get() = _groups.value
    
    
    fun updateUrgency(newValue: Boolean) {
        _isUrgent.value = newValue
    }
    
    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }
    
    fun updateContent(newContent: String) {
        _content.value = newContent
    }
    
    fun updateTags(newTags: List<String>) {
        _tags.value = newTags
    }
    
    fun updateStatus(newStatus: String) {
        _status.value = newStatus
    }
    
    fun updateUserIdentifiers(newIdentifiers: List<String>) {
        _userIdentifiers.value = newIdentifiers
    }
    
    fun updateGroups(newGroups: List<ExternalId>) {
        _groups.value = newGroups
    }
    
    fun addReaction(reaction: String) {
        _availableReactions.value += reaction
    }
    
    fun removeReaction(reaction: String) {
        _availableReactions.value = _availableReactions.value.filter { it != reaction }
    }
    
    fun toMessageData(author: UserData) = MessageData(
        title = title,
        author = author,
        content = content,
        isUrgent = isUrgent,
        tags = tags,
        status = null,
        userIdentifiers = userIdentifiers,
        availableReactions = availableReactions,
        groups = groups
    )
    
    fun clear() {
        _title.value = ""
        _content.value = ""
        _isUrgent.value = false
        _tags.value = emptyList()
        _status.value = ""
        _userIdentifiers.value = emptyList()
        _availableReactions.value = emptyList()
        _groups.value = emptyList()
    }
}