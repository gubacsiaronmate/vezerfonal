package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.v1.core.Table

object RegistrationCodes : Table("registration_code") {
    val id = integer("id").autoIncrement()
    
    val code = varchar("code", 255).uniqueIndex()
    val totalUses = integer("total_uses").default(1)
    val remainingUses = integer("remaining_uses")
    
    override val primaryKey = PrimaryKey(id)
}