package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.models.Organisation
import com.smokinggunstudio.vezerfonal.models.User
import org.jetbrains.exposed.v1.jdbc.Database

data class AuthResponse(
    val user: User,
    val db: Database,
    val org: Organisation,
    val rememberMe: Boolean = true
)
