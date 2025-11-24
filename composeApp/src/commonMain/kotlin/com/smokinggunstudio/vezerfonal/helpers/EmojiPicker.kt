package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.runtime.Composable

// Platform-specific emoji picker entrypoint
// Each platform should provide a native-feeling implementation.
expect object EmojiPicker {
    @Composable
    fun Show(
        isVisible: Boolean,
        onEmojiSelected: (String) -> Unit,
        onRemove: () -> Unit,
        onDismiss: () -> Unit,
    )
}
