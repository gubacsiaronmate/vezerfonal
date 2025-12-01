package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RecipientReactionBar(
    reactions: List<String>,
    onReactionSelected: (String?) -> Unit
) {
    val selected: MutableState<String?> = remember { mutableStateOf(null) }
    
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(100)
            )
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        reactions.forEach { emoji ->
            val isSelected = selected.value == emoji
            val bgColor =
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surfaceContainer
            
            Text(
                text = emoji,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(100)
                    )
                    .padding(8.dp)
                    .clickable {
                        if (isSelected) {
                            selected.value = null
                            onReactionSelected(null)
                        } else {
                            selected.value = emoji
                            onReactionSelected(emoji)
                        }
                    }
            )
        }
    }
}