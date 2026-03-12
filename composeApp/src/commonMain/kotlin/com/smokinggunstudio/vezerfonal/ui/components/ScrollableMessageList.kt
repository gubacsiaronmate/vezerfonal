package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.no_messages
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import com.smokinggunstudio.vezerfonal.enums.MessageStatus
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass

@Composable fun ScrollableMessageList(
    isSwipeable: Boolean,
    messages: List<MessageData>,
    onMessageClick: CallbackFunction<MessageData>,
    onArchive: CallbackFunction<MessageData> = CallbackFunction { },
    popUpContent: @Composable BoxScope.() -> Unit,
) {
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    Box(Modifier.fillMaxSize()) {
        if (messages.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inbox,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(Res.string.no_messages),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (isExpanded) Modifier.widthIn(max = 700.dp) else Modifier)
                    .align(Alignment.TopCenter),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(
                    items = messages.sortedBy { it.sentAt }.reversed(),
                    key = { it.externalId }
                ) { message ->
                    val item: @Composable () -> Unit = {
                        ListItem(
                            title = message.title,
                            author = message.author.name,
                            content = message.content,
                            sentAt = message.sentAt,
                            isUrgent = message.isUrgent,
                            isRead = message.status == MessageStatus.read,
                            onClick = { onMessageClick(message) },
                        )
                    }

                    if (isSwipeable) {
                        SwipeToArchiveRow({ onArchive(message) }) { item() }
                    } else {
                        item()
                    }
                }
            }
        }

        popUpContent()
    }
}
