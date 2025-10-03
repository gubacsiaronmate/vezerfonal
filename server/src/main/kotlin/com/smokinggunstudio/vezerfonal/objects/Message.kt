package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Message : Table("message") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val groupId = integer("group_id").references(Groups.id)
    
    val content = text("content")
    val isUrgent = bool("is_urgent").default(false)
    val authorUserId = integer("author_user_id").references(Users.id)
    
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}