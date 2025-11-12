package com.smokinggunstudio.vezerfonal.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

context(builder: StringBuilder)
operator fun String.unaryPlus(): StringBuilder = builder.append(this)

//operator fun StringBuilder.unaryPlus(value: String): StringBuilder = append(value)