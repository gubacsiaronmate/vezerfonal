package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.ui.screens.MessageViewScreen

data class ViewMessage(
    val accessToken: String,
    val isArchived: Boolean,
    val isSenderView: Boolean,
    val messageStr: String,
    val userIdentifier: String,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MessageViewScreen(
            accessToken = accessToken,
            isArchived = isArchived,
            messageStr = messageStr,
            isSenderView = isSenderView,
            userIdentifier = userIdentifier,
            onBack = { navigator.pop() },
        )
    }
}