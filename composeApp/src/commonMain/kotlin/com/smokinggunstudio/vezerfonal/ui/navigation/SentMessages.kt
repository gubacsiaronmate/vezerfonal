package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.LocalHttpClient
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.screens.SentMessagesScreen
import io.ktor.client.HttpClient

data class SentMessages(
    val accessToken: String,
    val onMessageClick: CallbackEvent<MessageData>,
    val scrollLockedBySliderCallback: CallbackEvent<Boolean>
) : Screen {
    @Composable
    override fun Content() {
        val client = LocalHttpClient.current
        
        SentMessagesScreen(
            client = client,
            accessToken = accessToken,
            onMessageClick = onMessageClick,
            scrollLockedBySliderCallback = scrollLockedBySliderCallback
        )
    }
}
