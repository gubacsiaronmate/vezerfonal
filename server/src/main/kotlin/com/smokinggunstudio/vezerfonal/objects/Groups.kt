package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.Modifiable
import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDate
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object Groups : Table("groups"), Modifiable<Int> {
    override val table: Table = this
    
    override val id = integer("id").autoIncrement()
    
    val displayName = varchar("display_name", 255)
    val description = text("description").default("")
    val groupAdminId = integer("group_admin_id").references(Users.id)
    val externalId = char("external_id", 16).uniqueIndex()
    val isInternal = bool("is_internal").default(false)
    
    @OptIn(ExperimentalTime::class)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}