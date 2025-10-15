package com.smokinggunstudio.vezerfonal.repositories

import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.objects.MessageTag
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllTags(context: CoroutineContext): List<Tag> = withContext(context) {
    return@withContext transaction { 
        val tags = MessageTag.selectAll()
        val connections = MessageTagConnection.selectAll().toList()
        return@transaction tags.map { tag -> Tag(
            id = tag[MessageTag.id],
            tagName = tag[MessageTag.name],
            messageIds = connections
                .filter { connection ->
                    connection[MessageTagConnection.tagId] == tag[MessageTag.id]
                }.map { it[MessageTagConnection.messageId] }
        ) }
    }
}