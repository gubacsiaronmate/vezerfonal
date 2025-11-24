package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.theme.Black
import com.smokinggunstudio.vezerfonal.helpers.EmojiPicker
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ReactionBar(
    onClick: CallbackClickEvent<String>
) {
    // track selected emoji for each of the 8 buttons
    val buttonEmojis = remember { List(8) { mutableStateOf<String?>(null) } }
    // simple inline picker visibility and active index
    val isPickerVisible = remember { mutableStateOf(false) }
    val activeIndex = remember { mutableStateOf<Int?>(null) }

    if (isPickerVisible.value && activeIndex.value != null) {
        val idx = activeIndex.value!!
        EmojiPicker.Show(
            isVisible = true,
            onEmojiSelected = { emoji ->
                val previous = buttonEmojis[idx].value
                if (previous != null && previous != emoji) {
                    onClick(previous) // signal removal of previous
                }
                buttonEmojis[idx].value = emoji
                onClick(emoji)
                isPickerVisible.value = false
            },
            onRemove = {
                val current = buttonEmojis[idx].value
                if (current != null) {
                    buttonEmojis[idx].value = null
                    onClick(current) // signal removal
                }
                isPickerVisible.value = false
            },
            onDismiss = { isPickerVisible.value = false }
        )
    }

    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(100)
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(8) { index ->
            val currentEmoji = buttonEmojis[index].value
            IconButton(
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