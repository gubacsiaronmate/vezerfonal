package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.ExternalId
import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.nowUTC
import com.smokinggunstudio.vezerfonal.ui.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.ui.state.model.WriteMessageStateModel
import kotlinx.datetime.LocalDateTime
import kotlin.collections.emptyList
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class WriteMessageStateController(initial: WriteMessageStateModel) {
    private val _title = mutableStateOf(initial.title)
    val title: String get() = _title.value
    
    private val _content = mutableStateOf(initial.content)
    val content: String get() = _content.value
    
    private val _isUrgent = mutableStateOf(initial.isUrgent)
    val isUrgent: Boolean get() = _isUrgent.value
    
    private val _tags = mutableStateOf(initial.tags)
    val tags: List<String> get() = _tags.value
    
    private val _status = mutableStateOf(initial.status)
    val status: String get() = _status.value

    private val _userIdentifiers = mutableStateOf(initial.userIdentifiers)
    val userIdentifiers: List<String> get() = _userIdentifiers.value
    
    // Array(8) { mutableStateOf(null) }
    private val _availableReactions: Array<MutableState<String?>> =
        initial.availableReactions.map { mutableStateOf(it) }.toTypedArray()
    val availableReactions: Array<MutableState<String?>> get() = _availableReactions
    
    private val _groups = mutableStateOf(initial.groups)
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
    
    fun addReaction(reaction: String, index: Int) {
        _availableReactions[index].value = reaction
    }
    
    fun removeReaction(index: Int) {
        _availableReactions[index].value = null
    }
    
    @OptIn(ExperimentalTime::class)
    fun toMessageData(author: UserData) = MessageData(
        title = title,
        author = author,
        content = content,
        isUrgent = isUrgent,
        tags = tags,
        status = MessageStatus.sent,
        userIdentifiers = userIdentifiers,
        availableReactions = availableReactions.ifNotEmpty(),
        groups = groups,
        sentAt = Clock.System.now().toEpochMilliseconds(),
        reactedWith = null,
        externalId = getExtId()
    )
    
    fun clear() {
        _title.value = ""
        _content.value = ""
        _isUrgent.value = false
        _tags.value = emptyList()
        _status.value = ""
        _userIdentifiers.value = emptyList()
        _availableReactions.forEach { it.value = null }
        _groups.value = emptyList()
    }
    
    fun snapshot() = WriteMessageStateModel(
        title = title,
        content = content,
        isUrgent = isUrgent,
        tags = tags,
        status = status,
        userIdentifiers = userIdentifiers,
        availableReactions = availableReactions.map { it.value }.toTypedArray(),
        groups = groups
    )
}