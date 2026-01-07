package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.ifFalseNull
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel

class RegisterStateController(initial: RegisterStateModel) {
    private val _extra = mutableStateOf(initial.extra)
    val extra: String get() = _extra.value
    
    private val _name = mutableStateOf(initial.name)
    val name: String get() = _name.value
    
    private val _email = mutableStateOf(initial.email)
    val email: String get() = _email.value
    
    private val _password = mutableStateOf(initial.password)
    val password: String get() = _password.value
    
    private val _identifier = mutableStateOf(initial.identifier)
    val identifier: String get() = _identifier.value
    
    private var isAdmin by mutableStateOf(initial.isAdmin)
    
    fun toUserData(): UserData = UserData(
        registrationCode = !isAdmin ifFalseNull extra,
        email = email,
        password = password,
        name = name,
        externalId = identifier,
        isAnyAdmin = isAdmin,
        isSuperAdmin = isAdmin
    )
    
    fun updateExtra(newValue: String) {
        val valid =
            newValue.all { it.isLetter() }
                    && newValue.length <= 100
        if (!valid) return
        _extra.value = newValue
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
    
    fun setIsAdmin(newValue: Boolean) {
        isAdmin = newValue
    }
    
    fun snapshot() = RegisterStateModel(extra, email, password, name, identifier)
}