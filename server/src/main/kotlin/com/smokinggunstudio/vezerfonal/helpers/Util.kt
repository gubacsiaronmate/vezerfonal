package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.models.JWTModel
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import java.util.*

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
): Query = selectWhere(SqlExpressionBuilder.where())

fun FieldSet.selectWhere(
    where: Op<Boolean>
): Query = Query(this, where)

fun LocalDateTime.before(
    instant: Instant
): Boolean = toInstant(
    TimeZone.currentSystemDefault()
) < instant

fun LocalDateTime.isExpired(): Boolean =
    before(Clock.System.now())

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

typealias SQLCondition = SqlExpressionBuilder.() -> Op<Boolean>

inline fun <reified T> List<T>.ifNotEmpty(): List<T>? = ifEmpty { null }