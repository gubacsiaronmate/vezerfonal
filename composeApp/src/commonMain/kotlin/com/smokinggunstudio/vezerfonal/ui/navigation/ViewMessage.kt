package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.screens.MessageViewScreen

data class ViewMessage(
    val accessToken: String,
    val message: MessageData
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        MessageViewScreen(client, accessToken, message)
    }
}