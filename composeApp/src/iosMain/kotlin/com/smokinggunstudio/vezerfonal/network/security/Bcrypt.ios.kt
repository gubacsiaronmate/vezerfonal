package com.smokinggunstudio.vezerfonal.network.security

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault
import platform.posix.crypt

private object BcryptBase64 {
    private const val BASE64_CODE = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    private val ENC = BASE64_CODE.toCharArray()

    fun encode(data: ByteArray): String {
        // Ported from jBCrypt's base64_encode with minor Kotlin adjustments
        val len = data.size
        val sb = StringBuilder()
        var off = 0
        var c1: Int
        var c2: Int
        while (off < len) {
            var o = data[off].toInt() and 0xff
            off++
            sb.append(ENC[o shr 2])
            o = (o and 0x03) shl 4
            if (off >= len) {
                sb.append(ENC[o])
                break
            }
            c1 = data[off].toInt() and 0xff
            off++
            o = o or (c1 shr 4)
            sb.append(ENC[o])
            o = (c1 and 0x0f) shl 2
            if (off >= len) {
                sb.append(ENC[o])
                break
            }
            c2 = data[off].toInt() and 0xff
            off++
            o = o or (c2 shr 6)
            sb.append(ENC[o])
            sb.append(ENC[c2 and 0x3f])
        }
        return sb.toString()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun generateSalt(cost: Int): String {
    val rounds = when {
        cost < 4 -> 4
        cost > 31 -> 31
        else -> cost
    }
    val saltBytes = ByteArray(16)
    // Fill salt with secure random bytes
    val rc = SecRandomCopyBytes(kSecRandomDefault, saltBytes.size.toULong(), saltBytes.refTo(0))
    if (rc != 0) {
        // Fallback to a simple pseudo-random if SecRandom fails (should be rare)
        for (i in saltBytes.indices) {
            saltBytes[i] = (i * 1103515245 + 12345).toByte()
        }
    }
    val saltB64 = BcryptBase64.encode(saltBytes)
    val costStr = if (rounds < 10) "0$rounds" else rounds.toString()
    return $$"$2b$$$costStr$$$saltB64"
}

@OptIn(ExperimentalForeignApi::class)
private fun cryptBcrypt(password: String, salt: String): String {
    val res = crypt(password, salt)
    require(res != null) { "bcrypt crypt() failed" }
    return res.toKString()
}

actual object Bcrypt {
    actual fun hashPassword(password: String, saltRounds: Int): String {
        val salt = generateSalt(saltRounds)
        return cryptBcrypt(password, salt)
    }
    
    actual fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val result = cryptBcrypt(password, hashedPassword)
        return result == hashedPassword
    }
}