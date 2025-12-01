package com.smokinggunstudio.vezerfonal.ui.state

import androidx.compose.runtime.mutableStateOf

class CreateOrganizationState {
    private val _organizationName = mutableStateOf("")
    val organizationName: String get() = _organizationName.value
    
    fun updateOrganizationName(newName: String) {
        _organizationName.value = newName
    }
}