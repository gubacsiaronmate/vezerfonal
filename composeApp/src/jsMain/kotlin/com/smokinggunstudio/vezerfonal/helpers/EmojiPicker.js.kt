package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

actual object EmojiPicker {
    @Composable
    actual fun Show(
        isVisible: Boolean,
        onEmojiSelected: (String) -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit,
    ) {
        if (!isVisible) return

        val emojiChoices = listOf(
            "😀","😂","😍","👍","🎉","🙏",
            "🔥","💯","😢","😡","👏","🤔",
            "🥳","🤝","✅","❌","🫶","✨"
        )

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Pick emoji") },
            text = {
                Column {
                    emojiChoices.chunked(6).forEach { row ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { emoji ->
                                TextButton(onClick = { onEmojiSelected(emoji) }) {
                                    Text(emoji)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onRemove) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        )
    }
}
