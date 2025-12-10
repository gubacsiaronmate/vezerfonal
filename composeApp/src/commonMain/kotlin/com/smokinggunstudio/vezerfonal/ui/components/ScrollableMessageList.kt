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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent
import com.smokinggunstudio.vezerfonal.ui.state.MessageFilterState

@Composable fun ScrollableMessageList(
    messages: List<MessageData>,
    onMessageClick: CallbackEvent<MessageData>,
    popUpContent: @Composable BoxScope.() -> Unit,
) {
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LazyColumn(modifier = Modifier.weight(1F)) {
                items(messages.reversed()) { message ->
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