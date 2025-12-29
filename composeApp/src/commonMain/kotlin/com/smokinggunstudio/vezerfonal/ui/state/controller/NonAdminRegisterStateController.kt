package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.state.model.AdminRegisterStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.NonAdminRegisterStateModel

class NonAdminRegisterStateController(initial: NonAdminRegisterStateModel) {
    private val _regCode = mutableStateOf(initial.regCode)
    val regCode: String get() = _regCode.value
    
    private val _name = mutableStateOf(initial.name)
    val name: String get() = _name.value
    
    private val _email = mutableStateOf(initial.email)
    val email: String get() = _email.value
    
    private val _password = mutableStateOf(initial.password)
    val password: String get() = _password.value
    
    private val _identifier = mutableStateOf(initial.identifier)
    val identifier: String get() = _identifier.value
    
    fun toUserData(): UserData = UserData(
        registrationCode = regCode,
        email = email,
        password = password,
        name = name,
        identifier = identifier,
        isAnyAdmin = false,
        isSuperAdmin = false
    )
    
    fun updateRegCode(newCode: String) {
        _regCode.value = newCode
    }
    
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
    
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    
    fun updateName(newName: String) {
        _name.value = newName
    }
    
    fun updateIdentifier(newIdentifier: String) {
        _identifier.value = newIdentifier
    }
    
    fun snapshot() = NonAdminRegisterStateModel(regCode, email, password, name, identifier)
}