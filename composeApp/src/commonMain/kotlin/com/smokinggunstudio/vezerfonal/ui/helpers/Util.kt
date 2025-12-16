package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.helpers.FileData
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable expect fun FileData.toImageResource(): ImageBitmap

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(this)
}

fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

@OptIn(ExperimentalUuidApi::class)
fun genRegCode() = Uuid.random().toString().replace("-","").substring(0..7)

@OptIn(ExperimentalTime::class)
fun Instant.between(start: Instant, end: Instant): Boolean = this in start..end

@OptIn(ExperimentalTime::class)
val List<MessageData>.earliestMessageTimestamp: Long?
    get() = this.minByOrNull { Instant.fromEpochMilliseconds(it.sentAt) }?.sentAt