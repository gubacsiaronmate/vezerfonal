package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokinggunstudio.vezerfonal.helpers.EmojiPicker
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEventIndexed
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction

@Composable
fun ReactionBar(
    buttonEmojis: Array<MutableState<String?>>,
    onSetEmoji: CallbackEventIndexed<String>,
    onClearEmoji: CallbackFunction<Int>
) {
    val isPickerVisible = remember { mutableStateOf(false) }
    val activeIndex = remember { mutableStateOf<Int?>(null) }

    if (isPickerVisible.value && activeIndex.value != null) {
        val idx = activeIndex.value!!
        EmojiPicker.Show(
            isVisible = true,
            onEmojiSelected = { emoji ->
                onSetEmoji(idx, emoji)
                isPickerVisible.value = false
            },
            onRemove = onRemove@{
                if (buttonEmojis[idx].value == null)
                    return@onRemove
                
                onClearEmoji(idx)
                isPickerVisible.value = false
            },
            onDismiss = { isPickerVisible.value = false }
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(50)
            ),
    ) {
        repeat(8) { i ->
            val currentEmoji = buttonEmojis[i].value
            IconButton(
                modifier = Modifier.widthIn(max = 32.dp),
                onClick = {
                    activeIndex.value = i
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