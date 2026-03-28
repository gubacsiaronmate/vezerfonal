package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.toUrlValidFormat
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.account

@Composable
fun SettingsNameCard(
    username: String,
    onClickFunction: Function,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickFunction() }
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfilePicture(
                    name = username.toUrlValidFormat(),
                    size = 64.dp,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                Column(modifier = Modifier.padding(start = Spacing.md)) {
                    Text(
                        text = stringResource(Res.string.account),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        text = username,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
