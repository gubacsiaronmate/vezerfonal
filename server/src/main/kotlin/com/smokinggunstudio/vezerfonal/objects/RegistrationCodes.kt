package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.sql.Table

object RegistrationCodes : Table("registration_code") {
    val id = integer("id").autoIncrement()
    
    val code = varchar("code", 255).uniqueIndex()
    val totalUses = integer("total_uses").default(1)
    val remainingUses = integer("remaining_uses").default(1)
    
    override val primaryKey = PrimaryKey(id)
}