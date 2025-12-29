package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.state.model.AdminRegisterStateModel

class AdminRegisterStateController(initial: AdminRegisterStateModel) {
    private val _orgName = mutableStateOf(initial.orgName)
    val orgName: String get() = _orgName.value
    
    private val _name = mutableStateOf(initial.name)
    val name: String get() = _name.value
    
    private val _email = mutableStateOf(initial.email)
    val email: String get() = _email.value
    
    private val _password = mutableStateOf(initial.password)
    val password: String get() = _password.value
    
    private val _identifier = mutableStateOf(initial.identifier)
    val identifier: String get() = _identifier.value
    
    fun toUserData(): UserData = UserData(
        registrationCode = null,
        email = email,
        password = password,
        name = name,
        identifier = identifier,
        isAnyAdmin = true,
        isSuperAdmin = true
    )
    
    fun updateOrgName(newValue: String) {
        val valid =
            newValue.all { it.isLetter() }
                    && newValue.length <= 100
        if (!valid) return
        _orgName.value = newValue
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
    
    fun snapshot() = AdminRegisterStateModel(orgName, email, password, name, identifier)
}