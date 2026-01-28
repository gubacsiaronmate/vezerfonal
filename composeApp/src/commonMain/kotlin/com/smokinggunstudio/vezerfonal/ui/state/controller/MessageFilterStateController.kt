package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.between
import com.smokinggunstudio.vezerfonal.ui.state.model.MessageFilterStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class MessageFilterStateController(initial: MessageFilterStateModel) {
    private val _earliestMessageUnixTime = mutableStateOf(initial.earliestMessageUnixTime)
    val earliestMessageUnixTime: Float get() = _earliestMessageUnixTime.value
    
    @OptIn(ExperimentalTime::class)
    val latestMessageUnixTime = initial.latestMessageUnixTime
    
    private val _selectedStartDate = mutableStateOf(initial.selectedStartDate)
    val selectedStartDate: Long get() = _selectedStartDate.value
    
    private val _selectedEndDate = mutableStateOf(initial.selectedEndDate)
    val selectedEndDate: Long get() = _selectedEndDate.value
    
    private val _senderName = mutableStateOf(initial.senderName)
    val senderName: String get() = _senderName.value
    
    private val _isImportant = mutableStateOf(initial.isImportant)
    val isImportant: Boolean get() = _isImportant.value
    
    private val _isWaitingForAnswer = mutableStateOf(initial.isWaitingForAnswer)
    val isWaitingForAnswer: Boolean get() = _isWaitingForAnswer.value
    
    private val _searchQuery = mutableStateOf(initial.searchQuery)
    val searchQuery: String get() = _searchQuery.value
    
    private val _tagSelectionState = mutableStateOf(initial.tagSelectionState)
    val tagSelectionState: TagSelectionStateModel = _tagSelectionState.value
    
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
        _tagSelectionState.value = TagSelectionStateModel()
    }
    
    fun snapshot(): MessageFilterStateModel = MessageFilterStateModel(
        earliestMessageUnixTime = earliestMessageUnixTime,
        latestMessageUnixTime = latestMessageUnixTime,
        selectedStartDate = selectedStartDate,
        selectedEndDate = selectedEndDate,
        senderName = senderName,
        isImportant = isImportant,
        isWaitingForAnswer = isWaitingForAnswer,
        searchQuery = searchQuery,
        tagSelectionState = tagSelectionState
    )
}