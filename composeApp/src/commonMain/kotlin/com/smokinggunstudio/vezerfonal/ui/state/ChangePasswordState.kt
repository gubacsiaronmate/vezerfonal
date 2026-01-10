package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class ChangePasswordState {
    private val _passwordChangeCode = mutableStateOf(0)
    val passwordChangeCode: Int get() = _passwordChangeCode.value
    
    private val _newPassword = mutableStateOf("")
    val newPassword: String get() = _newPassword.value
    
    private val _confirmPassword = mutableStateOf("")
    val confirmPassword: String get() = _confirmPassword.value
    
    fun updatePasswordChangeCode(newValue: String) {
        when {
            newValue.isEmpty() -> _passwordChangeCode.value = 0
            newValue.map { it.isDigit() }.all { it } ->
                _passwordChangeCode.value = newValue.toInt()
        }
    }
    
    fun updateNewPassword(newValue: String) {
        _newPassword.value = newValue
    }
    
    fun updateConfirmPassword(newValue: String) {
        _confirmPassword.value = newValue
    }
}