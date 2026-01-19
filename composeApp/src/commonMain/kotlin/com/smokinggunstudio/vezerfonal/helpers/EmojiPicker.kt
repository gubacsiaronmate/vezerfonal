package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.runtime.Composable

expect object EmojiPicker {
    @Composable
    fun Show(
        isVisible: Boolean,
        onEmojiSelected: (String) -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit,
    )
}
