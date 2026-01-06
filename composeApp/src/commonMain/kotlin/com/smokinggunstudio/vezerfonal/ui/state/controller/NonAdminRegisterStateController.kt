package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel

class NonAdminRegisterStateController(
    initial: RegisterStateModel.NonAdminRegisterStateModel
) : RegisterStateController {
    private val _regCode = mutableStateOf(initial.regCode)
    val regCode: String get() = _regCode.value
    
    private val _name = mutableStateOf(initial.name)
    override val name: String get() = _name.value
    
    private val _email = mutableStateOf(initial.email)
    override val email: String get() = _email.value
    
    private val _password = mutableStateOf(initial.password)
    override val password: String get() = _password.value
    
    private val _identifier = mutableStateOf(initial.identifier)
    override val identifier: String get() = _identifier.value
    
    override fun toUserData(): UserData = UserData(
        registrationCode = regCode,
        email = email,
        password = password,
        name = name,
        externalId = identifier,
        isAnyAdmin = false,
        isSuperAdmin = false
    )
    
    fun updateRegCode(newCode: String) {
        _regCode.value = newCode
    }
    
    override fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
    
    override fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    
    override fun updateName(newName: String) {
        _name.value = newName
    }
    
    override fun updateIdentifier(newIdentifier: String) {
        _identifier.value = newIdentifier
    }
    
    override fun snapshot() = RegisterStateModel.NonAdminRegisterStateModel(regCode, email, password, name, identifier)
}