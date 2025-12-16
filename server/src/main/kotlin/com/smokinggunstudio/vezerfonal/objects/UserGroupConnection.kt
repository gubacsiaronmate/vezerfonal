package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object UserGroupConnection : Table("user_group_connection") {
    val userId = integer("user_id").references(Users.id)
    val groupId = integer("group_id").references(Groups.id)
    val joinedAt = timestampWithTimeZone("joined_at").defaultExpression(CurrentTimestampWithTimeZone)
    
    override val primaryKey = PrimaryKey(userId, groupId)
}