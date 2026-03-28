package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable
fun SwipeToArchiveRow(
    onArchive: Function,
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
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Archive",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Archive,
                            contentDescription = "Archive",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
                else -> Unit
            }
        },
        onDismiss = { onArchive() }
    ) { content() }
}
