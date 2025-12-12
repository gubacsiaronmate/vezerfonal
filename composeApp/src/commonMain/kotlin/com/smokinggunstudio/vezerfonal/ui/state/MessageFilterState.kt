package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.helpers.now
import com.smokinggunstudio.vezerfonal.helpers.toFloat
import com.smokinggunstudio.vezerfonal.helpers.toInstant
import com.smokinggunstudio.vezerfonal.helpers.toLong
import kotlinx.datetime.LocalDateTime
import kotlin.math.floor
import kotlin.time.ExperimentalTime

class MessageFilterState {
    private val _earliestMessageUnixTime = mutableStateOf(0F)
    val earliestMessageUnixTime get() = _earliestMessageUnixTime.value
    
    val latestMessageUnixTime
        get() = mutableStateOf(LocalDateTime.now().toFloat()).value
    
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
    
    @OptIn(ExperimentalTime::class)
    fun setEarliestMessageUnixTime(newUnixTime: LocalDateTime?) {
        val nowInSeconds = LocalDateTime
            .now().toInstant()
            .toEpochMilliseconds() / 1000
        
        _earliestMessageUnixTime.value = newUnixTime?.toFloat()
            ?: (nowInSeconds - (60 * 10)).toFloat()
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