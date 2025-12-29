package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.smokinggunstudio.vezerfonal.data.UserData

@Deprecated(
    message = "Redone",
    level = DeprecationLevel.ERROR
)
class NonAdminRegisterState : RegisterState {
    private val _regCode = mutableStateOf("")
    val regCode: String get() = _regCode.value
    
    private val _email = mutableStateOf("")
    override val email: String get() = _email.value
    
    private val _password = mutableStateOf("")
    override val password: String get() = _password.value
    
    private val _name = mutableStateOf("")
    override val name: String get() = _name.value
    
    private val _identifier = mutableStateOf("")
    override val identifier: String get() = _identifier.value
    
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
    
    override fun toUserData(): UserData = UserData(
        registrationCode = regCode,
        email = email,
        password = password,
        name = name,
        identifier = identifier,
        isAnyAdmin = false,
        isSuperAdmin = false
    )
    
    companion object {
        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<NonAdminRegisterState, Any> = Saver(
            save = { state ->
                listOf(
                    state.regCode,
                    state.email,
                    state.password,
                    state.name,
                    state.identifier
                )
            },
            restore = { list ->
                val (regCode, email, password, name, identifier) = list as List<String>
                NonAdminRegisterState().apply {
                    updateRegCode(regCode)
                    updateEmail(email)
                    updatePassword(password)
                    updateName(name)
                    updateIdentifier(identifier)
                }
            }
        )
    }
}