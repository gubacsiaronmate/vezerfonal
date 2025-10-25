package com.smokinggunstudio.vezerfonal.helpers

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.RoutingContext

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

suspend inline fun <T> RoutingContext.tryOutgoing(
    onErrorMessage: String,
    action: () -> T
): T? = trial(
    onErrorMessage = onErrorMessage,
    statusCode = HttpStatusCode.InternalServerError,
    action = action
)