package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object JWTs : Table("jwt") {
    val id = char("id", 36)
    val tokenHash = char("token_hash", 64).uniqueIndex()
    val isRefresh = bool("is_refresh")
    
    val revoked = bool("revoked").default(false)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val expiresAt = datetime("expires_at")
    
    override val primaryKey = PrimaryKey(id)
}