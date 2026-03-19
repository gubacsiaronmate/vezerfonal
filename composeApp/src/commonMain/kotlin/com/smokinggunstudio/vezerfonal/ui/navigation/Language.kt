package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalPreferenceStorage
import com.smokinggunstudio.vezerfonal.helpers.applyLanguage
import com.smokinggunstudio.vezerfonal.ui.screens.LanguageScreen

object Language : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val prefStorage = LocalPreferenceStorage.current

        LanguageScreen(
            selectedTag = prefStorage.getLanguage(),
            onLanguageSelected = { tag ->
                prefStorage.saveLanguage(tag)
                applyLanguage(tag)
                navigator.pop()
            },
        )
    }
}
