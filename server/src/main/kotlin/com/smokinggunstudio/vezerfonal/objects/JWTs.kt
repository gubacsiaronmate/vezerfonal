package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.Modifiable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object JWTs : Table("jwt"), Modifiable<String> {
    override val table: Table = this
    
    override val id = char("id", 36)
    val tokenHash = char("token_hash", 64).uniqueIndex()
    val isRefresh = bool("is_refresh")
    val userId = integer("user_id").references(Users.id)
    
    val revoked = bool("revoked").default(false)
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    val expiresAt = timestampWithTimeZone("expires_at")
    
    override val primaryKey = PrimaryKey(id)
}