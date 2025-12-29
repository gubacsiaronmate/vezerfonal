package com.smokinggunstudio.vezerfonal.ui.state.model

import kotlinx.serialization.Serializable

@Serializable
data class AdminRegisterStateModel(
    val orgName: String = "",
    override val name: String = "",
    override val email: String = "",
    override val password: String = "",
    override val identifier: String = ""
) : RegisterStateModel
