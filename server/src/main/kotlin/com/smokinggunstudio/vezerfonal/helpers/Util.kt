package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.InteractionType
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.models.Message
import com.smokinggunstudio.vezerfonal.repositories.InteractionInfoRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.FieldSet
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.Query
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

suspend inline fun <T> RoutingContext.trial(
    onErrorMessage: String,
    statusCode: HttpStatusCode,
    action: () -> T,
): T? {
    return try { action() }
    catch (e: Exception) {
        call.respond(statusCode)
        log {
            "\n\n" + onErrorMessage + "\n\n" + e + "\n" + e.printStackTrace() + "\n\n"
        }
        null
    }
}

suspend inline fun <T> RoutingContext.tryIncoming(
    onErrorMessage: String,
    action: () -> T
): T? = trial(
    onErrorMessage = onErrorMessage,
    statusCode = HttpStatusCode.BadRequest,
    action = action
)

suspend inline fun <T> RoutingContext.tryInternal(
    onErrorMessage: String,
    action: () -> T
): T? = trial(
    onErrorMessage = onErrorMessage,
    statusCode = HttpStatusCode.InternalServerError,
    action = action
)

inline fun FieldSet.select(
    where: SQLCondition
): Query = selectWhere(where())

fun FieldSet.selectWhere(
    where: Op<Boolean>
): Query = Query(this, where)

@OptIn(ExperimentalTime::class)
fun Long.isExpired(): Boolean {
    val now = Clock.System.now().toEpochMilliseconds()
    return now >= this
}

typealias SQLCondition = () -> Op<Boolean>

inline fun <reified T : Table> makeArrayOfTable(vararg items: T): Array<T> = arrayOf(*items)

fun Query.singleOrNull(): ResultRow? = toList().ifNotEmpty()?.single()

@OptIn(ExperimentalTime::class)
fun Date.toKotlinInstant() = toInstant().toKotlinInstant()

@OptIn(ExperimentalTime::class)
fun OffsetDateTime.toKotlinInstant() = toInstant().toKotlinInstant()

@OptIn(ExperimentalTime::class)
fun Instant.toOffsetDateTime(zoneOffset: ZoneOffset): OffsetDateTime = toJavaInstant().atOffset(zoneOffset)

suspend inline fun Message.fillMissingInformation(db: Database, userId: Int, isSenderRequest: Boolean): MessageData {
    val interactions = InteractionInfoRepository(db)
        .getInteractionInfosByMessageAndUserId(this.id!!, userId)
    
    val reaction = interactions
        .singleOrNull { it.type == InteractionType.reaction }
        ?.reaction
    
    val status =
        if (isSenderRequest)
            MessageStatus.read
        else if (reaction != null)
            MessageStatus.read
        else interactions
            .filter { it.type == InteractionType.status }
            .maxByOrNull { it.createdAt }
            ?.status
            ?: MessageStatus.received
    
    return this.toDTO(reaction, status)
}

suspend inline fun List<Message>.fillMissingInfos(
    db: Database,
    userId: Int,
    isSenderRequest: Boolean
): List<MessageData> = map { message ->
    message.fillMissingInformation(db, userId, isSenderRequest)
}