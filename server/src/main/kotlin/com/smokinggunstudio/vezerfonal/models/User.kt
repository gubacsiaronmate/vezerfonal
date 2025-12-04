package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.Image
import com.smokinggunstudio.vezerfonal.security.hashPassword
import kotlinx.datetime.LocalDateTime

data class User(
    val id: Int?,
    val email: String,
    private var _password: String = "",
    val profilePic: Image?,
    val displayName: String,
    val identifier: String,
    var isAnyAdmin: Boolean?,
    val isSuperAdmin: Boolean = false,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    var password: String
        get() = _password
        set(value) { _password = hashPassword(value) }
    
    fun toDTO(): UserData = UserData(
        email = email,
        password = null,
        name = displayName,
        identifier = identifier,
        registrationCode = null,
        isAnyAdmin = isAnyAdmin ?: isSuperAdmin,
        isSuperAdmin = isSuperAdmin,
    )
}