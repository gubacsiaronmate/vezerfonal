package com.smokinggunstudio.vezerfonal.helpers

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

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