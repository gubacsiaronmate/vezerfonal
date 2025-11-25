package com.smokinggunstudio.vezerfonal.repositories

import ch.qos.logback.core.joran.conditional.Condition
import com.smokinggunstudio.vezerfonal.helpers.SQLCondition
import com.smokinggunstudio.vezerfonal.helpers.select
import com.smokinggunstudio.vezerfonal.models.Tag
import com.smokinggunstudio.vezerfonal.objects.MessageTag
import com.smokinggunstudio.vezerfonal.objects.MessageTagConnection
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext

suspend fun getAllTags(context: CoroutineContext): List<Tag> = withContext(context) {
    transaction {
        MessageTag.selectAll().map { tag ->
            Tag(
                id = tag[MessageTag.id],
                tagName = tag[MessageTag.name],
            )
        }
    }
}

suspend fun getTagByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): Tag? = withContext(context) {
    newSuspendedTransaction {
        MessageTag
            .select(condition)
            .firstOrNull()
            ?.let { tag ->
                Tag(
                    id = tag[MessageTag.id],
                    tagName = tag[MessageTag.name],
                )
            }
    }
}

suspend fun getTagsByCondition(
    context: CoroutineContext,
    condition: SQLCondition
): List<Tag> = withContext(context) {
    newSuspendedTransaction {
        MessageTag
            .select(condition)
            .map { tag ->
                Tag(
                    id = tag[MessageTag.id],
                    tagName = tag[MessageTag.name]
                )
            }
    }
}

suspend fun getTagByName(
    name: String,
    context: CoroutineContext
): Tag? = newSuspendedTransaction { getTagByCondition(context) { MessageTag.name eq name } }

suspend fun getTagsByMessageId(
    id: Int,
    context: CoroutineContext
): List<Tag> = newSuspendedTransaction { getTagsByCondition(context) { MessageTagConnection.messageId eq id } }