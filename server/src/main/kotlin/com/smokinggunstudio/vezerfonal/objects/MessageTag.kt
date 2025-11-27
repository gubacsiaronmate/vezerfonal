package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table

object MessageTag : Table("message_tag") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255).uniqueIndex()
    
    override val primaryKey = PrimaryKey(id)
    // TODO: gyakorisag
}