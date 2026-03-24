package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smokinggunstudio.vezerfonal.helpers.EmojiPicker
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEventIndexed
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.theme.ShapeFull
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.duplicate_reaction_error

@Composable
fun ReactionBar(
    buttonEmojis: Array<MutableState<String?>>,
    onSetEmoji: CallbackEventIndexed<String>,
    onClearEmoji: CallbackFunction<Int>,
    showBackground: Boolean = true,
    snackbarHostState: SnackbarHostState? = null,
) {
    val isPickerVisible = remember { mutableStateOf(false) }
    val activeIndex = remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()
    val duplicateMessage = stringResource(Res.string.duplicate_reaction_error)

    if (isPickerVisible.value && activeIndex.value != null) {
        val idx = activeIndex.value!!
        EmojiPicker.Show(
            isVisible = true,
            onEmojiSelected = { emoji ->
                val alreadyUsed = buttonEmojis.indices.any { j -> j != idx && buttonEmojis[j].value == emoji }
                if (alreadyUsed) {
                    scope.launch { snackbarHostState?.showSnackbar(duplicateMessage) }
                } else {
                    onSetEmoji(idx, emoji)
                }
                isPickerVisible.value = false
            },
            onRemove = onRemove@{
                if (buttonEmojis[idx].value == null)
                    return@onRemove
                onClearEmoji(idx)
                isPickerVisible.value = false
            },
            onDismiss = { isPickerVisible.value = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (showBackground)
                    Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer, shape = ShapeFull)
                else Modifier
            )
            .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        for (row in 0..1) {
            Row(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                for (col in 0..3) {
                    val i = row * 4 + col
                    val emoji = buttonEmojis[i].value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                activeIndex.value = i
                                isPickerVisible.value = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (emoji != null) {
                            Text(
                                text = emoji,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                            )
                        } else {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
