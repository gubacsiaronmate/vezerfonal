package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData

class AdminRegisterState : RegisterState {
    private val _orgName = mutableStateOf("")
    val orgName: String get() = _orgName.value
    
    private val _email = mutableStateOf("")
    override val email: String get() = _email.value
    
    private val _password = mutableStateOf("")
    override val password: String get() = _password.value
    
    private val _name = mutableStateOf("")
    override val name: String get() = _name.value
    
    private val _identifier = mutableStateOf("")
    override val identifier: String get() = _identifier.value
    
    override fun toUserData(): UserData = UserData(
        registrationCode = "",
        email = email,
        password = password,
        name = name,
        identifier = identifier,
        isSuperAdmin = true
    )
    
    fun updateOrgName(newCode: String) {
        _orgName.value = newCode
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
}