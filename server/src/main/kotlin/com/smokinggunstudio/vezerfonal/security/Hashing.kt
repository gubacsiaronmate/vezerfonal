package com.smokinggunstudio.vezerfonal.security

import at.favre.lib.crypto.bcrypt.BCrypt
import java.security.MessageDigest

fun hashPassword(password: String): String =
    BCrypt
        .withDefaults()
        .hashToString(12, password.toCharArray())
        .let { hash -> println(hash); hash }

fun verifyPassword(
    password: String,
    hashedPassword: String
): Boolean =
    BCrypt
        .verifyer()
        .verify(
            password.toCharArray(),
            hashedPassword
        )
        .let { result ->
            println("Valid format: ${result.validFormat}")
            println("Format error message: ${result.formatErrorMessage}")
            result.verified
        }

fun hashLongString(string: String): String =
    MessageDigest
        .getInstance("SHA-256")
        .digest(string.toByteArray())
        .joinToString("") { "%02x".format(it) }

fun verifyLongStringHash(
    string: String,
    hashedString: String
): Boolean = hashLongString(string) == hashedString