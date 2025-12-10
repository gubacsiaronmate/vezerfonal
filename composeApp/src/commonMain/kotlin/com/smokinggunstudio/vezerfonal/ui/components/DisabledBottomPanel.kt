package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ShapeModifier
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.marked_as_read
import vezerfonal.composeapp.generated.resources.you_reacted_with

@Composable
fun DisabledBottomPanel(
    reaction: String?
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = ShapeModifier.TOP_ROUNDED.toShape()
            )
            .border(
                shape = ShapeModifier.TOP_ROUNDED.toShape(),
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            ),
        verticalArrangement = Arrangement.Bottom
    )
    {
        if (reaction != null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = false,
                onClick = {}) {
                Text("${stringResource(Res.string.you_reacted_with)} $reaction")
            }
        }
        else {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = false,
                onClick = {}
            ) {
                Text(text = stringResource(Res.string.marked_as_read))
            }
        }
    }
}

@Preview
@Composable
private fun Asd() {
    DisabledBottomPanel(reaction = null)
}

@Preview
@Composable
private fun Asd2() {
    DisabledBottomPanel(reaction = "asd")
}