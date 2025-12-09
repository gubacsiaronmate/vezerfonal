package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ShapeModifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RecipientReactionBottomPanel(
    availableReactions: List<String>?,
    modifier: Modifier = Modifier,
    onIsReactionBarVisible: CallbackEvent<Boolean>,
    onReactionSelected: CallbackEvent<String>
) {
    var isReactionBarVisible by remember { mutableStateOf(false) }
    var selectedReaction by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = ShapeModifier.TOP_ROUNDED.toShape()
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = ShapeModifier.TOP_ROUNDED.toShape()
            ),
        verticalArrangement = Arrangement.Bottom,
    ) {
        if (isReactionBarVisible)
            availableReactions
                ?.let { reactions ->
                    RecipientReactionBar(reactions) {
                        selectedReaction = it
                        isReactionBarVisible = false
                        onReactionSelected(it)
                    }
                }
        Row {
            if (!availableReactions.isNullOrEmpty()) IconButton(
                onClick = { isReactionBarVisible = !isReactionBarVisible; onIsReactionBarVisible(isReactionBarVisible) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                if (selectedReaction == null)
                    Icon(
                        imageVector = Icons.Outlined.AddReaction,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                else
                    Text(
                        text = selectedReaction!!,
                    )
            }
            SlidingConfirmButton(8.dp, Modifier.height(64.dp)) {
                isReactionBarVisible = false
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Asd1() {
    RecipientReactionBottomPanel(
        listOf("👍", "❤️", "🔥", "👏", "😂", "😒", "😒", "😒"),
        onIsReactionBarVisible = {}
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun Asd2() {
    RecipientReactionBottomPanel(null, onIsReactionBarVisible = {}) {}
}

@Preview(showBackground = true)
@Composable
private fun Asd3() {
    RecipientReactionBottomPanel(emptyList(), onIsReactionBarVisible = {}) {}
}