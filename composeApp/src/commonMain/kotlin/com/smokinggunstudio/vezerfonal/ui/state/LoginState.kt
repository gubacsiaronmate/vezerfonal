package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class LoginState {
    private val _email = mutableStateOf("")
    val email: String get() = _email.value
    
    private val _password = mutableStateOf("")
    val password: String get() = _password.value
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
        
    fun updatePassword(newPassword: String) {
            _password.value = newPassword
        }
    }