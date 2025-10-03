package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.util.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserGroupConnection : Table("user_group_connection") {
    val userId = integer("user_id").references(Users.id)
    val groupId = integer("group_id").references(Groups.id)
    val joinedAt = datetime("joined_at").default(LocalDateTime.now())
    
    override val primaryKey = PrimaryKey(userId, groupId)
}