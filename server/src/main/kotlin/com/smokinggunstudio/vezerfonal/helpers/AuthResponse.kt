package com.smokinggunstudio.vezerfonal.helpers

import org.jetbrains.exposed.v1.jdbc.Database

data class AuthResponse(val userId: Int, val db: Database, val rememberMe: Boolean = true)
