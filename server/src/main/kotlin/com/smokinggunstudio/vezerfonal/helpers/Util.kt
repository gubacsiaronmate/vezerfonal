package com.smokinggunstudio.vezerfonal.helpers

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import java.util.Date

suspend inline fun <T> RoutingContext.trial(
    onErrorMessage: String,
    statusCode: HttpStatusCode,
    action: () -> T,
): T? {
    return try { action() }
    catch (e: Exception) { call.respondText(
        "$onErrorMessage Error: ${e.message}",
        status = statusCode
    ); println("\n\n$e\n${e.stackTrace.forEach { println(it) }}\n\n"); null }
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

inline fun FieldSet.select(where: SqlExpressionBuilder.() -> Op<Boolean>): Query = selectWhere(SqlExpressionBuilder.where())

fun FieldSet.selectWhere(where: Op<Boolean>): Query = Query(this, where)

fun LocalDateTime.before(instant: Instant): Boolean = toInstant(TimeZone.currentSystemDefault()) < instant

fun LocalDateTime.isExpired(): Boolean = before(Clock.System.now())

fun Date.compareTo(anotherDate: LocalDateTime): Boolean = toInstant().toKotlinInstant() == anotherDate.toInstant(TimeZone.currentSystemDefault())