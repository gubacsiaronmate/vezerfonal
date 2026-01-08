package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.Modifiable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object Users : Table("users"), Modifiable<Int> {
    override val table: Table = this
    
    override val id = integer("id").autoIncrement()
    
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val profilePicURI = varchar("profile_pic_uri", 255).uniqueIndex().nullable()
    val displayName = varchar("display_name", 255)
    val externalId = varchar("identifier", 255).uniqueIndex()
    val isSuperAdmin = bool("is_super_admin").default(false)
    
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    val updatedAt = timestampWithTimeZone("updated_at").defaultExpression(CurrentTimestampWithTimeZone)
    val deletedAt = timestampWithTimeZone("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}