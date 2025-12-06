package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.toInstant
import com.smokinggunstudio.vezerfonal.helpers.toLocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun Navigator.go(node: NavTree) = navigate(node.route)

val NavTree.route: String
    get() = when (hasParameters) {
        false -> "/${this::class.simpleName!!.lowercase()}"
        true -> "/${this::class.simpleName!!.lowercase()}/${getParameter()}"
    }

fun RouteBuilder.screen(route: NavTree, content: ComposableContent) {
    scene(route.route) { content() }
}

@Composable expect fun FileData.toImageResource(): ImageBitmap

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return emailRegex.matches(this)
}

fun String.capitalize(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

@OptIn(ExperimentalUuidApi::class)
fun genRegCode() = Uuid.random().toString().replace("-","").substring(0..7)

fun LocalDateTime.between(start: LocalDateTime, end: LocalDateTime): Boolean =
    start.toInstant() < this.toInstant() && this.toInstant() < end.toInstant()

/** MINUTES */
@OptIn(ExperimentalTime::class)
fun Long.toLocalDateTime(): LocalDateTime = Instant.fromEpochSeconds(this * 60).toLocalDateTime()

fun String?.toLDTOrNull(): LocalDateTime? = this?.let { LocalDateTime.parse(it) }

fun String.toLDT(): LocalDateTime = LocalDateTime.parse(this)