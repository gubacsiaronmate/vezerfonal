package com.smokinggunstudio.vezerfonal.objects

import org.jetbrains.exposed.sql.Table

object MessageTagConnection : Table("message_tag_connection") {
    val messageId = integer("message_id").references(Message.id)
    val tagId = integer("tag_id").references(MessageTag.id)
    
    override val primaryKey = PrimaryKey(messageId, tagId)
}