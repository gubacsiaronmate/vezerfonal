package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.admin_reg_code_management_desc
import vezerfonal.composeapp.generated.resources.admin_tag_management_desc
import vezerfonal.composeapp.generated.resources.admin_tools
import vezerfonal.composeapp.generated.resources.admin_user_management_desc
import vezerfonal.composeapp.generated.resources.registration_code_management
import vezerfonal.composeapp.generated.resources.tag_management
import vezerfonal.composeapp.generated.resources.user_management

@Composable
fun AdminToolsScreen(
    onUserManagementClick: Function,
    onTagManagementClick: Function,
    onRegistrationCodeManagementClick: Function
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(
            text = stringResource(Res.string.admin_tools),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = Spacing.sm),
        )
        AdminToolCard(
            icon = Icons.Outlined.Person,
            title = stringResource(Res.string.user_management),
            subtitle = stringResource(Res.string.admin_user_management_desc),
            onClick = onUserManagementClick,
        )
        AdminToolCard(
            icon = Icons.Outlined.Sell,
            title = stringResource(Res.string.tag_management),
            subtitle = stringResource(Res.string.admin_tag_management_desc),
            onClick = onTagManagementClick,
        )
        AdminToolCard(
            icon = Icons.Outlined.Key,
            title = stringResource(Res.string.registration_code_management),
            subtitle = stringResource(Res.string.admin_reg_code_management_desc),
            onClick = onRegistrationCodeManagementClick,
        )
    }
}

@Composable
private fun AdminToolCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: Function,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.lg),
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(26.dp),
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.size(14.dp),
            )
        }
    }
}