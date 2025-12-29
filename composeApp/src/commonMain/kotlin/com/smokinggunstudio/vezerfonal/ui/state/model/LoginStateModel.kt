package com.smokinggunstudio.vezerfonal.ui.state.model

data class LoginStateModel(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = true
)
