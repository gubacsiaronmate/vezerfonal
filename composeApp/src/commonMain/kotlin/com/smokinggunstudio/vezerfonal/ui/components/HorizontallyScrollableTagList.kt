package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.MessageData
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HorizontallyScrollableTagList(
    tags: List<String>
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .horizontalScroll(rememberScrollState())
    ) {
        tags.forEach { tag ->
            Row(modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                Text(tag,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}