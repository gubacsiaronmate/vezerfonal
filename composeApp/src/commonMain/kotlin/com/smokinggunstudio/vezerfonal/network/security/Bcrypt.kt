package com.smokinggunstudio.vezerfonal.network.security

expect object Bcrypt {
    fun hashPassword(password: String, saltRounds: Int = 12): String
    fun verifyPassword(password: String, hashedPassword: String): Boolean
}