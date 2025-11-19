package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.identifier
import vezerfonal.composeapp.generated.resources.name

@Preview(showBackground = true)
@Composable
fun SettingsNameCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePicture(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(100.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.name),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    modifier = Modifier
                        .padding(4.dp)
                )
                Text(
                    text = stringResource(Res.string.identifier),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
        Image(
            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.size(24.dp)
        )
    }
}