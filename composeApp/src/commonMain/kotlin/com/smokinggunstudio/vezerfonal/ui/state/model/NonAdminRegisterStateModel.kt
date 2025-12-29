package com.smokinggunstudio.vezerfonal.ui.state.model

data class NonAdminRegisterStateModel(
    val regCode: String = "",
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val identifier: String = ""
) : RegisterStateModel
