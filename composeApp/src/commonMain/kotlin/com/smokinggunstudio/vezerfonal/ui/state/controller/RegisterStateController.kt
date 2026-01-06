package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel

interface RegisterStateController {
    val name: String
    
    val email: String
    
    val password: String
    
    val identifier: String
    
    fun toUserData(): UserData
    
    fun updateEmail(newEmail: String)
    
    fun updatePassword(newPassword: String)
    
    fun updateName(newName: String)
    
    fun updateIdentifier(newIdentifier: String)
    
    fun snapshot(): RegisterStateModel
}