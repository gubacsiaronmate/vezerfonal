package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.Modifiable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import kotlin.time.ExperimentalTime

object Groups : Table("groups"), Modifiable<Int> {
    override val table: Table = this
    
    override val id = integer("id").autoIncrement()
    
    val displayName = varchar("display_name", 255)
    val description = text("description").default("")
    val groupAdminId = integer("group_admin_id").references(Users.id)
    val externalId = char("external_id", 16).uniqueIndex()
    val isInternal = bool("is_internal").default(false)
    
    @OptIn(ExperimentalTime::class)
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    val updatedAt = timestampWithTimeZone("updated_at").defaultExpression(CurrentTimestampWithTimeZone)
    val deletedAt = timestampWithTimeZone("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}