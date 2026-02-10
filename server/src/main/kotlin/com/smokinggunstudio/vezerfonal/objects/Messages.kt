package com.smokinggunstudio.vezerfonal.objects

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import org.jetbrains.exposed.v1.json.jsonb

object Messages : Table("message") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id).nullable()
    val groupId = integer("group_id").references(Groups.id).nullable()
    
    val title = text("title")
    val content = text("content")
    val isUrgent = bool("is_urgent").default(false)
    val authorUserId = integer("author_user_id").references(Users.id)
    val externalId = char("external_id", 16).uniqueIndex()
    val availableReactions = jsonb(
        name = "available_reactions",
        serialize = { list: List<String> -> Json.encodeToString(list) },
        deserialize = { str: String -> Json.decodeFromString<List<String>>(str) },
    ).nullable()
    
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    val updatedAt = timestampWithTimeZone("updated_at").defaultExpression(CurrentTimestampWithTimeZone)
    val deletedAt = timestampWithTimeZone("deleted_at").nullable()
    
    override val primaryKey = PrimaryKey(id)
}