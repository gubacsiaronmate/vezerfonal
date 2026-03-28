package com.smokinggunstudio.vezerfonal

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.smokinggunstudio.vezerfonal.helpers.PreferenceStorage
import org.jetbrains.compose.resources.painterResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.app_icon
import java.util.Locale

fun main() {
    PreferenceStorage().getLanguage()?.let { Locale.setDefault(Locale.forLanguageTag(it)) }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "vezerfonal",
            icon = painterResource(Res.drawable.app_icon),
        ) {
            App()
        }
    }
}
