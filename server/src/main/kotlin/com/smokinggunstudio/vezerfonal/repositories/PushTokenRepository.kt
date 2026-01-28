package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.PushToken
import org.jetbrains.exposed.v1.jdbc.Database

class PushTokenRepository(val db: Database) {
    suspend fun registerToken(
        token: PushToken,
    ) {}
}