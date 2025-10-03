package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.sql.Table

object MessageTag : Table("message_tag") {
    val id = integer("id").autoIncrement()
    val tag = varchar("tag", 255)
    
    override val primaryKey = PrimaryKey(id)
}