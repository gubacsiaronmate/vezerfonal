package com.smokinggunstudio.vezerfonal.security

import at.favre.lib.crypto.bcrypt.BCrypt

fun hashPassword(password: String): String =
    BCrypt.withDefaults().hash(12, password.toCharArray()).toString()

fun verifyPassword(password: String, hashedPassword: String): Boolean =
    BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified