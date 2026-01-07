package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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

val List<MessageData>.earliestMessageTimestamp: Long?
    get() = this.minByOrNull { it.sentAt }?.sentAt

inline fun <reified T> Array<MutableState<T?>>.ifNotEmpty(): List<T>? {
    if (all { it.value == null }) return null
    
    val result = mutableListOf<T>()
    forEach { item -> item.value?.let { result.add(it) } }
    return result
}

inline fun <reified T> Array<MutableState<T?>>.contains(item: T): Boolean = any { it.value == item }

infix fun <T> Boolean.ifFalseNull(value: T): T? = if (this) value else null