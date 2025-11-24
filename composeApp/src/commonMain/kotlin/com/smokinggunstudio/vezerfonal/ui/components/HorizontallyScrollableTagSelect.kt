package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.tags

@Composable
fun HorizontallyScrollableTagSelect() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        for (i in 1..10) {
            IconToggleButton(
                colors = IconButtonDefaults.iconToggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.padding(8.dp)
                    .width(90.dp),
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null
                        )
                        Text(text = stringResource(Res.string.tags),
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    }
}