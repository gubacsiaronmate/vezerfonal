package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.security.Bcrypt

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
        registrationCode = null,
        email = email,
        password = Bcrypt.hashPassword(password),
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