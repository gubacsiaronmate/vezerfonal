package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.components.SettingsNameCard
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Preview
@Composable
fun SettingsScreen(
    onClickEvent: ClickEvent
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SettingsNameCard(onClickEvent)
        SettingRow(
            imageVector = Icons.Default.Archive,
            text = stringResource(Res.string.archive),
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
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier
                        .height(24.dp)
                )
            }
        )
    }
}