package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object Organisations : Table("organisation") {
    val id = integer("id").autoIncrement()
    
    val name = varchar("name", 255)
    val externalId = char("external_id", 16).uniqueIndex()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    
    override val primaryKey = PrimaryKey(id)
}