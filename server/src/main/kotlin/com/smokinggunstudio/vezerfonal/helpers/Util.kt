package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.models.JWTModel
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.v1.core.FieldSet
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.statements.DeleteStatement.Companion.where
import org.jetbrains.exposed.v1.jdbc.Query
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

suspend inline fun <T> RoutingContext.trial(
    onErrorMessage: String,
    statusCode: HttpStatusCode,
    action: () -> T,
): T? {
    return try { action() }
    catch (e: Exception) { call.respondText(
        "$onErrorMessage Error: ${e.message}",
        status = statusCode
    ); println("\n\n$e\n${ e.printStackTrace() }\n\n"); null }
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
fun LocalDateTime.before(
    instant: Instant
): Boolean = toInstant(
    TimeZone.currentSystemDefault()
) < instant

@OptIn(ExperimentalTime::class)
fun LocalDateTime.isExpired(): Boolean =
    before(Clock.System.now())

@OptIn(ExperimentalTime::class)
fun Date.compareTo(
    anotherDate: LocalDateTime
): Boolean =
    toInstant().toKotlinInstant().let { println(it); it } == anotherDate.toInstant(
        TimeZone.currentSystemDefault()
    ).let { println(it); it }

private typealias jtLDT = java.time.LocalDateTime

fun List<JWTModel>.latestPair(): TokenResponse? {
    if (isEmpty()) return null
    if (size == 2) return TokenResponse(
        accessToken = this[indexOfFirst { !it.isRefresh }].tokenHash,
        refreshToken = this[indexOfFirst { it.isRefresh }].tokenHash
    )
    
    val maxTs = maxOfOrNull { it.createdAt ?: jtLDT.MIN.toKotlinLocalDateTime() }
    val latest = filter { it.createdAt == maxTs }
    
    if (latest.size != 2) return null
    
    return TokenResponse(
        accessToken = latest[latest.indexOfFirst { !it.isRefresh }].tokenHash,
        refreshToken = latest[latest.indexOfFirst { it.isRefresh }].tokenHash
    )
}

typealias SQLCondition = () -> Op<Boolean>

inline fun <reified T> List<T>.ifNotEmpty(): List<T>? = ifEmpty { null }

inline fun <reified T : Table> makeArrayOfTable(vararg items: T): Array<T> = arrayOf(*items)

fun Query.toSingle(): ResultRow? = toList().ifNotEmpty()?.single()
