package com.smokinggunstudio.vezerfonal.ui.state.controller

import androidx.compose.runtime.mutableStateOf
import com.smokinggunstudio.vezerfonal.ui.state.model.CreateOrgStateModel

class CreateOrgStateController(initial: CreateOrgStateModel) {
    private val _organizationName = mutableStateOf(initial.orgName)
    val organizationName: String get() = _organizationName.value
    
    fun updateOrganizationName(newName: String) {
        _organizationName.value = newName
    }
    
    fun snapshot() = CreateOrgStateModel(organizationName)
}