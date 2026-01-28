package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table

object PushTokens : Table("push_tokens") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val token = text("fcm_token")
    val platform = varchar("platform", 50)
    
    override val primaryKey = PrimaryKey(id)
}