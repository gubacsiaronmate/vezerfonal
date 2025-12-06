package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.screens.MessageViewScreen

class ViewMessage(val message: MessageData) : Screen {
    @Composable
    override fun Content() = MessageViewScreen(message)
}