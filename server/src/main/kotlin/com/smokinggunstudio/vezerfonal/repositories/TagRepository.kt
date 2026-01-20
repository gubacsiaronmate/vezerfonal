package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.ifNotEmpty
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.objects.MessageTag
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update

class TagRepository(val db: Database) {
    private fun ResultRow.toTag(): Tag = Tag(
        id = this@toTag[MessageTag.id],
        name = this@toTag[MessageTag.name],
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
        (MessageTag innerJoin MessageTagConnection)
            .select { MessageTagConnection.messageId eq id }
            .map { it.toTag() }
    }
    
    suspend fun doesTagExist(
        name: String
    ): Boolean = suspendTransaction(db) { getTagByName(name) != null }
    
    suspend fun insertTag(
        tag: Tag
    ): Boolean = suspendTransaction(db) {
        if (doesTagExist(tag.name))
            return@suspendTransaction false
        
        MessageTag.insert {
            it[name] = tag.name
        }.insertedCount == 1
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
    
    suspend fun modifyTag(
        tag: Tag
    ): Boolean = suspendTransaction(db) {
        MessageTag.update({ MessageTag.name eq tag.name }) {
            it[name] = tag.name
        } == 1
    }
    
    suspend fun deleteTag(
        tag: Tag
    ): Boolean = suspendTransaction(db) {
        val del1 = MessageTagConnection.deleteWhere { MessageTag.name eq tag.name } > 0
        val del2 = MessageTag.deleteWhere { MessageTag.name eq tag.name } == 1
        del1 && del2
    }
}