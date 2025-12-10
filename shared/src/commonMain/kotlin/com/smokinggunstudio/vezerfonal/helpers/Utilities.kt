package com.smokinggunstudio.vezerfonal.helpers

import com.smokinggunstudio.vezerfonal.data.DTO
import com.smokinggunstudio.vezerfonal.data.OrgData
import kotlinx.datetime.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

context(builder: StringBuilder)
operator fun String.unaryPlus(): StringBuilder = builder.append(this)

/**
 * @return [Instant] based on [TimeZone.currentSystemDefault].
 */
fun LocalDateTime.toInstant(): Instant = toInstant(TimeZone.currentSystemDefault())

/**
 * @return [LocalDateTime] based on [TimeZone.currentSystemDefault].
 * */
fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.toFloat(): Float = toInstant().epochSeconds.toFloat()

fun LocalDateTime.toLong(): Long = toInstant().epochSeconds

fun Long.toInstant(): Instant = Instant.fromEpochSeconds(this)

@OptIn(ExperimentalUuidApi::class)
fun getExtId(): String = Uuid.random().toString().split("-").joinToString("").substring(0..15)

typealias Identifier = String

typealias ExternalId = String

inline fun <reified T> List<T>.ifNotEmpty(): List<T>? = ifEmpty { null }

inline fun <reified T : DTO> Map<String, Any?>.toDTO(): T = Json.decodeFromString<T>(Json.encodeToString(this))