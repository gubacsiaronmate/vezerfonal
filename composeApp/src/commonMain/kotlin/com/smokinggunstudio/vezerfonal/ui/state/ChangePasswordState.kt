package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class ChangePasswordState {
    private val _currentPassword = mutableStateOf("")
    val currentPassword: String get() = _currentPassword.value
    
    private val _newPassword = mutableStateOf("")
    val newPassword: String get() = _newPassword.value
    
    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: String get() = _confirmPassword.value
    
    fun updateCurrentPassword(newPassword: String) {
        _currentPassword.value = newPassword
    }
    
    fun updateNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }
    
    fun updateConfirmPassword(newPassword: String) {
        _confirmPassword.value = newPassword
    }
}