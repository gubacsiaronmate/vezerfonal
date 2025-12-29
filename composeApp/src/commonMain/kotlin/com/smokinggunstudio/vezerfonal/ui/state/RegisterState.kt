package com.smokinggunstudio.vezerfonal.ui.state

import com.smokinggunstudio.vezerfonal.data.UserData
import kotlinx.serialization.Serializable

@Deprecated(
    message = "Redone",
    level = DeprecationLevel.ERROR
)
@Serializable
interface RegisterState {
    val email: String
    val password: String
    val name: String
    val identifier: String
    
    fun updateEmail(newEmail: String)
    
    fun updatePassword(newPassword: String)
    
    fun updateName(newName: String)
    
    fun updateIdentifier(newIdentifier: String)
    
    fun toUserData(): UserData
}