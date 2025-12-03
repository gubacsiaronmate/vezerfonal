package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.helpers.EmojiPicker
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ReactionBar(
    onClick: CallbackEvent<String>
) {
    val buttonEmojis = remember { List(8) { mutableStateOf<String?>(null) } }
    val isPickerVisible = remember { mutableStateOf(false) }
    val activeIndex = remember { mutableStateOf<Int?>(null) }

    if (isPickerVisible.value && activeIndex.value != null) {
        val idx = activeIndex.value!!
        EmojiPicker.Show(
            isVisible = true,
            onEmojiSelected = { emoji ->
                val previous = buttonEmojis[idx].value
                if (previous != null && previous != emoji) {
                    onClick(previous)
                }
                buttonEmojis[idx].value = emoji
                onClick(emoji)
                isPickerVisible.value = false
            },
            onRemove = {
                val current = buttonEmojis[idx].value
                if (current != null) {
                    buttonEmojis[idx].value = null
                    onClick(current)
                }
                isPickerVisible.value = false
            },
            onDismiss = { isPickerVisible.value = false }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(50)
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(8) { index ->
            val currentEmoji = buttonEmojis[index].value
            IconButton(
                modifier = Modifier.width(32.dp),
                onClick = {
                    activeIndex.value = index
                    isPickerVisible.value = true
                }
            ) {
                if (currentEmoji != null) {
                    Text(
                        text = currentEmoji,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
    }
}