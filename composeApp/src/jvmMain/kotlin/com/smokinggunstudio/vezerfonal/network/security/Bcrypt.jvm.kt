package com.smokinggunstudio.vezerfonal.network.security

import at.favre.lib.crypto.bcrypt.BCrypt

actual object Bcrypt {
    actual fun hashPassword(password: String, saltRounds: Int): String =
        BCrypt.withDefaults().hash(saltRounds, password.toByteArray()).toString()
    
    actual fun verifyPassword(password: String, hashedPassword: String): Boolean =
        BCrypt.verifyer().verify(password.toByteArray(), hashedPassword.toByteArray()).verified
}