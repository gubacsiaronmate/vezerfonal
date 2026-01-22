package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.helpers.toSerialized
import com.smokinggunstudio.vezerfonal.ui.screens.SentMessagesScreen

data class SentMessages(
    val accessToken: String,
    val userIdentifier: String,
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        val navigator = LocalNavigator.currentOrThrow
        
        SentMessagesScreen(
            accessToken = accessToken,
            onMessageClick = {
                navigator.push(
                    ViewMessage(
                        accessToken = accessToken,
                        isArchived = false,
                        messageStr = it.toSerialized(),
                        isSenderView = true,
                        userIdentifier = userIdentifier
                    )
                )
            },
        )
    }
}
