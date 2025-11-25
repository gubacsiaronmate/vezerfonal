package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.objects.MessageTag
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private fun ResultRow.toTag(): Tag = Tag(
    id = this@toTag[MessageTag.id],
    tagName = this@toTag[MessageTag.name],
)

suspend fun getAllTags(): List<Tag> =
    newSuspendedTransaction {
        MessageTag
            .selectAll()
            .map { it.toTag() }
    }

suspend fun getTagByCondition(
    condition: SQLCondition
): Tag? = newSuspendedTransaction {
    MessageTag
        .select(condition)
        .toList()
        .ifNotEmpty()
        ?.single()
        ?.toTag()
}

suspend fun getTagsByCondition(
    condition: SQLCondition
): List<Tag> = newSuspendedTransaction {
    MessageTag
        .select(condition)
        .map { it.toTag() }
}

suspend fun getTagByName(
    name: String,
): Tag? = newSuspendedTransaction { getTagByCondition { MessageTag.name eq name } }

suspend fun getTagsByMessageId(
    id: Int,
): List<Tag> = newSuspendedTransaction {
    getTagsByCondition {
        (MessageTagConnection.messageId eq id) and
        (MessageTag.id eq MessageTagConnection.tagId)
    }
}

suspend fun doesTagExist(
    name: String
): Boolean = newSuspendedTransaction { getTagByName(name) != null }

suspend fun insertTag(
    tag: Tag
): Boolean = newSuspendedTransaction {
    if (!doesTagExist(tag.tagName))
        MessageTag.insert {
            it[name] = tag.tagName
        }.insertedCount == 1
    else false
}

suspend fun attachTagToMessageId(
    newMessageId: Int,
    newTagId: Int
): Boolean = newSuspendedTransaction {
    MessageTagConnection.insert {
        it[messageId] = newMessageId
        it[tagId] = newTagId
    }.insertedCount == 1
}

suspend fun attachTagsToMessageId(
    newMessageId: Int,
    tagIds: List<Int>
): Boolean = newSuspendedTransaction {
    tagIds.map { id ->
        attachTagToMessageId(
            newMessageId = newMessageId,
            newTagId = id
        )
    }.all { it }
}