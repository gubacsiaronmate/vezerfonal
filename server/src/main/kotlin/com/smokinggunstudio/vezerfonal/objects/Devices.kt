package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Devices : Table("devices") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val pushToken = varchar("push_token", 255).uniqueIndex()
    val sessionId = integer("session_id").references(Sessions.id).nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()

    override val primaryKey = PrimaryKey(id)
}