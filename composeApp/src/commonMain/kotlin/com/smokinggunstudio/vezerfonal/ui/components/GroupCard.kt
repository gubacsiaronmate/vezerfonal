package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups2
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.members_label

@Composable
fun GroupCard(
    name: String,
    extId: String,
    description: String,
    amITheAdmin: Boolean,
    memberCount: Int = 0,
    alwaysShowId: Boolean = false,
) {
    val canShowId = amITheAdmin || alwaysShowId
    val clipboardManager = LocalClipboardManager.current
    var showCopied by remember { mutableStateOf(false) }

    LaunchedEffect(showCopied) {
        if (showCopied) {
            delay(1500)
            showCopied = false
        }
    }

    val cardContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.lg, vertical = Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.large,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Groups2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (memberCount > 0) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "$memberCount ${stringResource(Res.string.members_label)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else if (description.isNotBlank()) {
                    Spacer(Modifier.height(Spacing.xs))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (canShowId) {
                    Spacer(Modifier.height(Spacing.xs))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        if (showCopied) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(10.dp),
                            )
                        }
                        Text(
                            text = extId,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (showCopied)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        )
                    }
                }
            }
            if (amITheAdmin)
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                )
        }
    }

    val cardModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Spacing.md, vertical = Spacing.xs)
    val cardShape = MaterialTheme.shapes.large
    val cardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
    )
    val cardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    if (canShowId) {
        Card(
            onClick = {
                clipboardManager.setText(AnnotatedString(extId))
                showCopied = true
            },
            modifier = cardModifier,
            shape = cardShape,
            colors = cardColors,
            elevation = cardElevation,
        ) { cardContent() }
    } else {
        Card(
            modifier = cardModifier,
            shape = cardShape,
            colors = cardColors,
            elevation = cardElevation,
        ) { cardContent() }
    }
}
