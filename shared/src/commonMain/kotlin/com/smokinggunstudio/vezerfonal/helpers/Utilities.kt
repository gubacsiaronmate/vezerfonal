package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.DTO
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.nowDefault(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.nowUTC(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)

context(builder: StringBuilder)
operator fun String.unaryPlus(): StringBuilder = builder.append(this)

/**
 * @return [LocalDateTime] based on [TimeZone.currentSystemDefault].
 * */
@OptIn(ExperimentalTime::class)
fun Instant.toLocalDateTimeDefault(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

@OptIn(ExperimentalUuidApi::class)
fun getExtId(): String = Uuid.random().toString().replace("-", "").substring(0..15)

typealias Identifier = String

typealias ExternalId = String

inline fun <reified T> List<T>.ifNotEmpty(): List<T>? = ifEmpty { null }

inline fun <reified T : DTO> String.toDTO(): T = Json.decodeFromString<T>(this)

inline fun log(message: () -> String) = println("\n\n\n\n\n${message()}\n\n\n\n\n")