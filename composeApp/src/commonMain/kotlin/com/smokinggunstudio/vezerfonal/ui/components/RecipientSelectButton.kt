package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.Function

@Composable
fun RecipientSelectButton(
    text: String,
    selectedAmount: Int,
    onClick: Function,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        border = BorderStroke(
            color = MaterialTheme.colorScheme.outlineVariant,
            width = 1.dp,
        ),
        modifier = modifier.height(48.dp),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            VerticalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                text = selectedAmount.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
