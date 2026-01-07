package com.smokinggunstudio.vezerfonal.ui.state.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterStateModel(
    val extra: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val identifier: String = "",
    val isAdmin: Boolean = false
)