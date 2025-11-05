package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class LoginState {
    private val _email = mutableStateOf("")
    val email: String get() = _email.value
    
    
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
    
    
}
