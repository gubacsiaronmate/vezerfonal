package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.helpers.Image
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Required

data class User(
    val id: Int,
    val registrationCode: RegistrationCode,
    val email: String,
    private var _password: String,
    val profilePic: Image,
    val displayName: String,
    val identifier: String,
    val isSuperAdmin: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?
) {
    @Required var password: String
        get() = _password
        set(value) { _password = TODO() }
}