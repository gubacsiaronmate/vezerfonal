package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object Organisations : Table("organisation") {
    val id = integer("id").autoIncrement()
    
    val name = varchar("name", 255)
    val externalId = char("external_id", 16).uniqueIndex()
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    
    override val primaryKey = PrimaryKey(id)
}