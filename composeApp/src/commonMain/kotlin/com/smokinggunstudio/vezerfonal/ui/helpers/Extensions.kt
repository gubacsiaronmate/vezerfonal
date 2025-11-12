package com.smokinggunstudio.vezerfonal.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import com.smokinggunstudio.vezerfonal.helpers.FileData
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

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