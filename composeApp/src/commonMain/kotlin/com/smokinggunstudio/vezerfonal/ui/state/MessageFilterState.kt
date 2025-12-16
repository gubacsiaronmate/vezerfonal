package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MessageFilterState {
    private val _earliestMessageUnixTime = mutableStateOf(0F)
    val earliestMessageUnixTime get() = _earliestMessageUnixTime.value
    
    @OptIn(ExperimentalTime::class)
    val latestMessageUnixTime
        get() = mutableStateOf(Clock.System.now().toEpochMilliseconds().toFloat()).value
    
    private val _selectedStartDate = mutableStateOf(0L)
    val selectedStartDate get() = _selectedStartDate.value
    
    private val _selectedEndDate = mutableStateOf(0L)
    val selectedEndDate get() = _selectedEndDate.value
    
    private val _senderName = mutableStateOf("")
    val senderName get() = _senderName.value
    
    private val _isImportant = mutableStateOf(false)
    val isImportant get() = _isImportant.value
    
    private val _isWaitingForAnswer = mutableStateOf(false)
    val isWaitingForAnswer get() = _isWaitingForAnswer.value
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery get() = _searchQuery.value
    
    val tagSelectionState = mutableStateOf(TagSelectionState()).value
    
    private val _isRangeSet = mutableStateOf(false)
    val isRangeSet: Boolean get() = _isRangeSet.value
    
    @OptIn(ExperimentalTime::class)
    fun setEarliestMessageUnixTime(newUnixTime: Long?) {
        _earliestMessageUnixTime.value = newUnixTime?.toFloat()
            ?: Clock.System.now().toEpochMilliseconds().toFloat()
    }
    
    fun updateSelectedStartDate(newValue: Long) {
        _selectedStartDate.value = newValue
        _isRangeSet.value = selectedStartDate > 0L && selectedEndDate > 0L
    }
    
    fun updateSelectedEndDate(newValue: Long) {
        _selectedEndDate.value = newValue
        _isRangeSet.value = selectedStartDate > 0L && selectedEndDate > 0L
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
    
    fun clear() {
        updateSelectedStartDate(earliestMessageUnixTime.toLong())
        updateSelectedEndDate(latestMessageUnixTime.toLong())
        updateSenderName("")
        updateIsWaitingForAnswer(false)
        updateIsImportant(false)
        updateSearchQuery("")
        tagSelectionState.clear()
    }
}