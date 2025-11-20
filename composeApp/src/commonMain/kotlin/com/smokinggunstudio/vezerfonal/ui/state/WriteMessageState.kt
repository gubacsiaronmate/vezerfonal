package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class WriteMessageState {
    private val _subject = mutableStateOf("")
    val subject: String get() = _subject.value
    
    private val _message = mutableStateOf("")
    val message: String get() = _message.value
    
    fun updateSubject(newSubject: String) {
        _subject.value = newSubject
    }
    
    fun updateMessage(newMessage: String) {
        _message.value = newMessage
    }
}