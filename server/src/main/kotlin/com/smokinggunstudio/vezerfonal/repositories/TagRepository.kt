package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.objects.MessageTag
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

class TagRepository(val db: Database) {
    private fun ResultRow.toTag(): Tag = Tag(
        id = this@toTag[MessageTag.id],
        tagName = this@toTag[MessageTag.name],
    )
    
    suspend fun getAllTags(): List<Tag> =
        suspendTransaction(db) {
            MessageTag
                .selectAll()
                .map { it.toTag() }
        }
    
    suspend fun getTagByCondition(
        condition: SQLCondition
    ): Tag? = suspendTransaction(db) {
        MessageTag
            .select(condition)
            .toList()
            .ifNotEmpty()
            ?.single()
            ?.toTag()
    }
    
    suspend fun getTagsByCondition(
        condition: SQLCondition
    ): List<Tag> = suspendTransaction(db) {
        MessageTag
            .select(condition)
            .map { it.toTag() }
    }
    
    suspend fun getTagByName(
        name: String,
    ): Tag? = suspendTransaction(db) { getTagByCondition { MessageTag.name eq name } }
    
    suspend fun getTagsByMessageId(
        id: Int,
    ): List<Tag> = suspendTransaction(db) {
        getTagsByCondition {
            (MessageTagConnection.messageId eq id) and
                    (MessageTag.id eq MessageTagConnection.tagId)
        }
    }
    
    suspend fun doesTagExist(
        name: String
    ): Boolean = suspendTransaction(db) { getTagByName(name) != null }
    
    suspend fun insertTag(
        tag: Tag
    ): Boolean = suspendTransaction(db) {
        if (!doesTagExist(tag.tagName))
            MessageTag.insert {
                it[name] = tag.tagName
            }.insertedCount == 1
        else false
    }
    
    suspend fun attachTagToMessageId(
        newMessageId: Int,
        newTagId: Int
    ): Boolean = suspendTransaction(db) {
        MessageTagConnection.insert {
            it[messageId] = newMessageId
            it[tagId] = newTagId
        }.insertedCount == 1
    }
    
    suspend fun attachTagsToMessageId(
        newMessageId: Int,
        tagIds: List<Int>
    ): Boolean = suspendTransaction(db) {
        tagIds.map { id ->
            attachTagToMessageId(
                newMessageId = newMessageId,
                newTagId = id
            )
        }.all { it }
    }
}