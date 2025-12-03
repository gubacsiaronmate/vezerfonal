package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object UserGroupConnection : Table("user_group_connection") {
    val userId = integer("user_id").references(Users.id)
    val groupId = integer("group_id").references(Groups.id)
    val joinedAt = datetime("joined_at").defaultExpression(CurrentDateTime)
    
    override val primaryKey = PrimaryKey(userId, groupId)
}