package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.models.Organisation
import org.jetbrains.exposed.v1.jdbc.Database

data class AuthResponse(
    val userId: Int,
    val db: Database,
    val org: Organisation,
    val rememberMe: Boolean = true
)
