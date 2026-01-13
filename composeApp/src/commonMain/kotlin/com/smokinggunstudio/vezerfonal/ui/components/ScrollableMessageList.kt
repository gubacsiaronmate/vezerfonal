package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction

@Composable fun ScrollableMessageList(
    isSwipeable: Boolean,
    messages: List<MessageData>,
    onMessageClick: CallbackFunction<MessageData>,
    onArchive: CallbackFunction<MessageData>,
    popUpContent: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LazyColumn(modifier = Modifier.weight(1F)) {
                items(messages.reversed()) { message ->
                    if (isSwipeable)
                        SwipeToArchiveRow({ onArchive(message) }) {
                            ListItem(
                                title = message.title,
                                author = message.author.name,
                                onClick = { onMessageClick(message) }
                            )
                        }
                    else
                        ListItem(
                            title = message.title,
                            author = message.author.name,
                            onClick = { onMessageClick(message) }
                        )
                }
            }
        }
        
        popUpContent()
    }
}