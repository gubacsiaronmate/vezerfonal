package com.smokinggunstudio.vezerfonal.objects

import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.PGEnum
import com.smokinggunstudio.vezerfonal.helpers.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

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
    val actorUserId = integer("actor_user_id").references(Users.id).nullable()
    
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
    
    init {
        uniqueIndex(messageId, userId)
    }
    
    override val primaryKey = PrimaryKey(id)
}