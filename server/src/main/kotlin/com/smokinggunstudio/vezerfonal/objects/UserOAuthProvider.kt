package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table

object UserOAuthProvider : Table("user_oauth_provider") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val providerName = varchar("provider_name", 255)
    val providerUserId = varchar("provider_user_id", 255)
    
    init {
        uniqueIndex(providerName, providerUserId)
    }
    
    override val primaryKey = PrimaryKey(id)
}