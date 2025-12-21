package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable
fun SwipeToArchiveRow(
    onArchive: ClickEvent,
    modifier: Modifier = Modifier,
    content: ComposableContent
) {
    val state = rememberSwipeToDismissBoxState(
        SwipeToDismissBoxValue.Settled
    ) { distance -> distance * 0.35f }
    
    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            when (state.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "Archive",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2E7D32))
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(end = 20.dp)
                    )
                }
                else -> Unit
            }
        },
        onDismiss = { onArchive() }
    ) { content() }
}
