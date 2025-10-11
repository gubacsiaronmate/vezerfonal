package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.helpers.ImageHelper
import kotlinx.datetime.LocalDateTime

data class User(
    val id: Int,
    val registrationCode: RegistrationCode,
    val email: String,
    val password: String? = null,
    val profilePic: ImageHelper,
    val displayName: String,
    val identifier: String,
    val isSuperAdmin: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime
)