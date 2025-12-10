package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.PGEnum
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object MessageUserInteractions : Table("message_user_interactions") {
    val id = integer("id").autoIncrement()
    val messageId = integer("message_id").references(Messages.id)
    val userId = integer("user_id").references(Users.id)
    
    val type = customEnumeration(
        name = "type",
        sql = "interaction_type",
        fromDb = { value -> InteractionType.valueOf(value as String) },
        toDb = { PGEnum("interaction_type", it) }
    )
    val status = customEnumeration(
        name = "status",
        sql = "message_status",
        fromDb = { value -> MessageStatus.valueOf(value as String) },
        toDb = { PGEnum("message_status", it) }
    ).nullable()
    val reaction = varchar("reaction", 255).nullable()
    val recipientUserId = integer("recipient_user_id").references(Users.id).nullable()
    
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
    val deletedAt = datetime("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}