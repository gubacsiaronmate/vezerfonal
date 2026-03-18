package com.smokinggunstudio.vezerfonal.helpers.security

import com.smokinggunstudio.vezerfonal.helpers.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec

actual class TokenStorage {
    private val storageDir = File(System.getProperty("user.home"), ".config/vezerfonal")
    private val keystoreFile = File(storageDir, "vault.p12")
    private val tokensFile = File(storageDir, "tokens.enc")

    // Derive a PKCS12 password from OS user identity via PBKDF2.
    // This ties the keystore to the OS user account — the file cannot be
    // trivially decrypted after being copied to a different user/machine.
    private fun deriveKeystorePassword(): CharArray {
        val passphrase = "${System.getProperty("user.name")}:${System.getProperty("user.home")}"
        val salt = "vezerfonal-ks".toByteArray(Charsets.UTF_8)
        val spec = PBEKeySpec(passphrase.toCharArray(), salt, 65_536, 256)
        val raw = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(raw).toCharArray()
    }

    // Return the persisted AES-256 key, creating and storing it on first run.
    private fun getOrCreateAesKey(): SecretKey {
        storageDir.mkdirs()
        val password = deriveKeystorePassword()
        val keystore = KeyStore.getInstance("PKCS12")
        if (keystoreFile.exists()) {
            keystoreFile.inputStream().use { keystore.load(it, password) }
        } else {
            keystore.load(null, password)
        }
        val alias = "token-encryption-key"
        return if (keystore.containsAlias(alias)) {
            val protection = KeyStore.PasswordProtection(password)
            (keystore.getEntry(alias, protection) as KeyStore.SecretKeyEntry).secretKey
        } else {
            val key = KeyGenerator.getInstance("AES").apply { init(256) }.generateKey()
            keystore.setEntry(
                alias,
                KeyStore.SecretKeyEntry(key),
                KeyStore.PasswordProtection(password)
            )
            keystoreFile.outputStream().use { keystore.store(it, password) }
            key
        }
    }

    // AES-256-GCM encryption. A fresh random 12-byte IV is prepended to each ciphertext.
    // GCM provides authenticated encryption — tampered ciphertext throws on decryption.
    private fun encrypt(plaintext: String): String {
        val key = getOrCreateAesKey()
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(iv + ciphertext)
    }

    private fun decrypt(encoded: String): String? = runCatching {
        val key = getOrCreateAesKey()
        val combined = Base64.getDecoder().decode(encoded)
        val iv = combined.copyOfRange(0, 12)
        val ciphertext = combined.copyOfRange(12, combined.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        cipher.doFinal(ciphertext).toString(Charsets.UTF_8)
    }.getOrNull()

    actual suspend fun saveTokens(tokens: TokenResponse) = withContext(Dispatchers.IO) {
        tokensFile.writeText(encrypt(Json.encodeToString(tokens)))
    }

    actual suspend fun getTokens(): TokenResponse? = withContext(Dispatchers.IO) {
        if (!tokensFile.exists()) return@withContext null
        val json = decrypt(tokensFile.readText()) ?: return@withContext null
        runCatching { Json.decodeFromString<TokenResponse>(json) }.getOrNull()
    }

    actual suspend fun clearTokens(): Boolean = withContext(Dispatchers.IO) {
        tokensFile.delete()
    }
}
