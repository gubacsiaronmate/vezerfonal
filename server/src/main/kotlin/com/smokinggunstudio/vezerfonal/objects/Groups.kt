package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Groups : Table("groups") {
    val id = integer("id").autoIncrement()
    
    val displayName = varchar("display_name", 255)
    val description = text("description").default("")
    val groupAdminId = integer("group_admin_id").references(Users.id)
    val isInternal = bool("is_internal").default(false)
    
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}