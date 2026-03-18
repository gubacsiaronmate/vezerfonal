package com.smokinggunstudio.vezerfonal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.app_icon

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "vezerfonal",
        icon = painterResource(Res.drawable.app_icon),
    ) {
        App()
    }
}
