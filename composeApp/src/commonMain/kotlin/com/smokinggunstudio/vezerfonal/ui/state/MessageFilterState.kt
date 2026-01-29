package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MessageFilterState(private val tagList: List<TagData>) {
    private val _earliestMessageUnixTime = mutableStateOf(0F)
    val earliestMessageUnixTime: Float get() = _earliestMessageUnixTime.value
    
    @OptIn(ExperimentalTime::class)
    val latestMessageUnixTime = Clock.System.now().toEpochMilliseconds().toFloat()
    
    private val _selectedStartDate = mutableStateOf(0L)
    val selectedStartDate: Long get() = _selectedStartDate.value
    
    private val _selectedEndDate = mutableStateOf(0L)
    val selectedEndDate: Long get() = _selectedEndDate.value
    
    private val _senderName = mutableStateOf("")
    val senderName: String get() = _senderName.value
    
    private val _isImportant = mutableStateOf(false)
    val isImportant: Boolean get() = _isImportant.value
    
    private val _isWaitingForAnswer = mutableStateOf(false)
    val isWaitingForAnswer: Boolean get() = _isWaitingForAnswer.value
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery: String get() = _searchQuery.value
    
    private val _tagSelectionState = mutableStateOf(TagSelectionStateModel(allItems = tagList))
    val tagSelectionState: TagSelectionStateModel get() = _tagSelectionState.value
    
    @OptIn(ExperimentalTime::class)
    fun setEarliestMessageUnixTime(newUnixTime: Long?) {
        _earliestMessageUnixTime.value = newUnixTime?.toFloat()
            ?: Clock.System.now().toEpochMilliseconds().toFloat()
    }
    
    fun updateSelectedStartDate(newValue: Long) {
        _selectedStartDate.value = newValue
    }
    
    fun updateSelectedEndDate(newValue: Long) {
        _selectedEndDate.value = newValue
    }
    
    fun updateSenderName(newValue: String) {
        _senderName.value = newValue
    }
    
    fun updateIsImportant(newValue: Boolean) {
        _isImportant.value = newValue
    }
    
    fun updateIsWaitingForAnswer(newValue: Boolean) {
        _isWaitingForAnswer.value = newValue
    }
    
    fun updateSearchQuery(newValue: String) {
        _searchQuery.value = newValue
    }
    
    fun updateTagSelectionState(newValue: TagSelectionStateModel) {
        _tagSelectionState.value = newValue
    }
    
    fun clear() {
        updateSelectedStartDate(earliestMessageUnixTime.toLong())
        updateSelectedEndDate(latestMessageUnixTime.toLong())
        updateSenderName("")
        updateIsWaitingForAnswer(false)
        updateIsImportant(false)
        updateSearchQuery("")
        _tagSelectionState.value = TagSelectionStateModel(allItems = tagList)
    }
}