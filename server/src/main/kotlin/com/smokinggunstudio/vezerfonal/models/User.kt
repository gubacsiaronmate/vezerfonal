package com.smokinggunstudio.vezerfonal.models

import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.Image
import com.smokinggunstudio.vezerfonal.security.hashPassword
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class User @OptIn(ExperimentalTime::class) constructor(
    val id: Int?,
    val email: String,
    private var _password: String = "",
    val profilePic: Image?,
    val displayName: String,
    val externalId: String,
    var isAnyAdmin: Boolean?,
    val isSuperAdmin: Boolean = false,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val deletedAt: Instant?
) {
    var password: String
        get() = _password
        set(value) { _password = hashPassword(value) }
    
    fun toDTO(): UserData = UserData(
        email = email,
        password = null,
        name = displayName,
        externalId = externalId,
        registrationCode = null,
        isAnyAdmin = isAnyAdmin ?: isSuperAdmin,
        isSuperAdmin = isSuperAdmin,
    )
}