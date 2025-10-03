package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Session : Table("session") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val jti = varchar("jti", 255).uniqueIndex()
    val refreshTokenHash = varchar("refresh_token_hash", 255).uniqueIndex()
    val ipAddress = varchar("ip_address", 255).nullable()
    val revoked = bool("revoked")
    val deviceInfo = text("device_info")
    val lastUsedAt = datetime("last_used_at")
    val expiresAt = datetime("expires_at")
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}