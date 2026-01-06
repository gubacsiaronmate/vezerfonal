package com.smokinggunstudio.vezerfonal.ui.state.model

sealed class RegisterStateModel {
    abstract val name: String
    abstract val email: String
    abstract val password: String
    abstract val identifier: String
    
    data class AdminRegisterStateModel(
        val orgName: String = "",
        override val name: String = "",
        override val email: String = "",
        override val password: String = "",
        override val identifier: String = "",
    ) : RegisterStateModel()
    
    data class NonAdminRegisterStateModel(
        val regCode: String = "",
        override val name: String = "",
        override val email: String = "",
        override val password: String = "",
        override val identifier: String = "",
    ) : RegisterStateModel()
}