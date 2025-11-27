package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime

object JWTs : Table("jwt") {
    val id = char("id", 36)
    val tokenHash = char("token_hash", 64).uniqueIndex()
    val isRefresh = bool("is_refresh")
    val userId = integer("user_id").references(Users.id)
    
    val revoked = bool("revoked").default(false)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val expiresAt = datetime("expires_at")
    
    override val primaryKey = PrimaryKey(id)
}