package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class ChangePasswordState {
    private val _passwordChangeCode = mutableStateOf(0)
    val passwordChangeCode: Int get() = _passwordChangeCode.value
    
    private val _newPassword = mutableStateOf("")
    val newPassword: String get() = _newPassword.value
    
    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: String get() = _confirmPassword.value
    
    fun updatePasswordChangeCode(newPassword: String) {
        _passwordChangeCode.value = newPassword.toInt()
    }
    
    fun updateNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }
    
    fun updateConfirmPassword(newPassword: String) {
        _confirmPassword.value = newPassword
    }
}