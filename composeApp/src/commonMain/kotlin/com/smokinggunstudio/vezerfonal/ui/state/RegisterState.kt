package com.smokinggunstudio.vezerfonal.ui.state

interface RegisterState {
    val email: String
    val password: String
    val name: String
    val identifier: String
    
    fun updateEmail(newEmail: String)
    
    fun updatePassword(newPassword: String)
    
    fun updateName(newName: String)
    
    fun updateIdentifier(newIdentifier: String)
}