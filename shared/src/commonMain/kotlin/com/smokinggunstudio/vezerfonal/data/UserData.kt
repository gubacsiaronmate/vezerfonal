package com.smokinggunstudio.vezerfonal.data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val email: String,
    val password: String?,
    override val name: String,
    val identifier: String,
    val isAnyAdmin: Boolean,
    val isSuperAdmin: Boolean,
) : DTO