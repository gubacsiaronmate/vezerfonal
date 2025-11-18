package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Preview
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(Res.string.name),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(Res.string.identifier),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Image(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        }
        SettingRow(
            imageVector = Icons.Default.Archive,
            text = stringResource(Res.string.archive)
        )
        SettingRow(
            imageVector = Icons.Outlined.Notifications,
            text = stringResource(Res.string.notifications)
        )
        SettingRow(
            imageVector = Icons.AutoMirrored.Outlined.Article,
            text = stringResource(Res.string.terms_of_service)
        )
        SettingRow(
            imageVector = Icons.Outlined.Language,
            text = stringResource(Res.string.language)
        )
        SettingRow(
            imageVector = Icons.Outlined.DarkMode,
            text = stringResource(Res.string.dark_mode),
            trailing = @Composable {
                Switch(
                    checked = false,
                    onCheckedChange = {},
                    modifier = Modifier
                        .height(24.dp)
                )
            }
        )
    }
}