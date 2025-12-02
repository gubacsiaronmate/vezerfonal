package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.datetime.*
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

@OptIn(ExperimentalUuidApi::class)
fun getExtId(): String = Uuid.random().toString().split("_").joinToString().substring(0..15)