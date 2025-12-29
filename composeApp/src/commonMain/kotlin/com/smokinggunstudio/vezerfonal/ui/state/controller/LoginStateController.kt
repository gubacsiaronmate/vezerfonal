package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.ui.state.model.LoginStateModel

class LoginStateController(initial: LoginStateModel) {
    private val _email = mutableStateOf(initial.email)
    val email: String get() = _email.value
    
    private val _password = mutableStateOf(initial.password)
    val password: String get() = _password.value
    
    private val _rememberMe = mutableStateOf(initial.rememberMe)
    val rememberMe: Boolean get() = _rememberMe.value
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
        
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    
    fun updateRememberMe(newValue: Boolean) {
        _rememberMe.value = newValue
    }
    
    fun snapshot() = LoginStateModel(email, password, rememberMe)
}