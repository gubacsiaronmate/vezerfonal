package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.FileMetaData
import com.smokinggunstudio.vezerfonal.helpers.toLDTDefault
import com.smokinggunstudio.vezerfonal.ui.state.model.RegisterStateModel
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.read
import vezerfonal.composeapp.generated.resources.received
import vezerfonal.composeapp.generated.resources.sent
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

expect fun FileData.toImageResource(): ImageBitmap

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

fun RegisterStateModel.toSerialized(): String = Json.encodeToString(this)

fun String.toModel(): RegisterStateModel = Json.decodeFromString(this)

internal inline val MessageStatus.asStr: String
    @Composable get() = when (this) {
        MessageStatus.sent -> stringResource(Res.string.sent)
        MessageStatus.received -> stringResource(Res.string.received)
        MessageStatus.read -> stringResource(Res.string.read)
    }

fun MessageData.changeStatus(status: MessageStatus) = MessageData(
    title = this.title,
    sentAt = this.sentAt,
    content = this.content,
    author = this.author,
    isUrgent = this.isUrgent,
    tags = this.tags,
    externalId = this.externalId,
    reactedWith = this.reactedWith,
    status = status,
    groups = this.groups,
    userIdentifiers = this.userIdentifiers,
    availableReactions = this.availableReactions
)

inline val Long.asFormattedLDTStr: String
    get() {
        val local = Instant
            .fromEpochMilliseconds(this)
            .toLDTDefault()
        
        return "${
            local.date.toString().replace("-", ". ")
        }. ${
            local.hour.toString().padStart(2, '0')
        }:${
            local.minute.toString().padStart(2, '0')
        }"
    }

fun <T> List<T>.limit(limit: Int): List<T> =
    if (size > limit) subList(0, limit) else this

expect fun String.svgXMLToByteArray(
    size: Int,
    quality: Int = 90
): ByteArray

fun ByteArray.toFileData(
    fileName: String
): FileData = FileData(
    bytes = this,
    metaData = FileMetaData(
        name = fileName,
        mimeType = "application/octet-stream"
    )
)

expect fun String.toAscii(): String

fun String.toUrlValidFormat(): String = trim().replace(" ", "").lowercase().toAscii()