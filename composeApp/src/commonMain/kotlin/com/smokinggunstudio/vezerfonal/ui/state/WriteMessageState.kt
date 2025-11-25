package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import kotlin.collections.emptyList

class WriteMessageState {
    private val _subject = mutableStateOf("")
    val subject: String get() = _subject.value
    
    private val _message = mutableStateOf("")
    val message: String get() = _message.value
    
    private val _reactions = mutableStateOf<List<String?>>(emptyList())
    val reactions: List<String?> get() = _reactions.value
    
    private val _isUrgent = mutableStateOf(false)
    val isUrgent: Boolean get() = _isUrgent.value
    
    fun updateUrgency(newValue: Boolean) {
        _isUrgent.value = newValue
    }
    
    fun addReaction(newReaction: String) {
        _reactions.value += newReaction
    }
    
    fun removeReaction(reaction: String) {
        _reactions.value = _reactions.value.filter { it != reaction }
    }
    
    fun updateSubject(newSubject: String) {
        _subject.value = newSubject
    }
    
    fun updateMessage(newMessage: String) {
        _message.value = newMessage
    }
}