package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun RecipientReactionBottomPanel() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(color = MaterialTheme.colorScheme.surface)
        .border(1.dp, MaterialTheme.colorScheme.onSurface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = {}) {
            Text(text = "Send") }
        Button(
            onClick = {}) {
            Text(text = "Cancel") }
    }
}