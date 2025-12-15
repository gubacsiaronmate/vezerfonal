package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Outbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.components.SettingsNameCard
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.HomeCache
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Composable
fun SettingsScreen(
    user: UserData,
    onAccountSettingsClick: ClickEvent,
    onAdminToolsClick: ClickEvent,
    onArchiveClick: ClickEvent,
    onNotificationsClick: ClickEvent,
    onTOSClick: ClickEvent,
    onLanguageClick: ClickEvent,
    onSentMessagesClick: ClickEvent,
    isInDarkTheme: Boolean,
    onThemeSwitchClick: CallbackEvent<Boolean>,
) {
    var checked by remember { mutableStateOf(isInDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        SettingsNameCard(user.name, onAccountSettingsClick)
        
        if (user.isSuperAdmin)
            SettingRow(
                imageVector = Icons.Outlined.AdminPanelSettings,
                text = stringResource(Res.string.admin_tools),
                onClick = onAdminToolsClick
            )
        
        if (user.isAnyAdmin)
            SettingRow(
                imageVector = Icons.Outlined.Outbox,
                text = stringResource(Res.string.sent_messages),
                onClick = onSentMessagesClick
            )
        
        SettingRow(
            imageVector = Icons.Outlined.Archive,
            text = stringResource(Res.string.archive),
            onClick = onArchiveClick
        )
        
        SettingRow(
            imageVector = Icons.Outlined.Notifications,
            text = stringResource(Res.string.notifications),
            onClick = onNotificationsClick
        )
        
        SettingRow(
            imageVector = Icons.AutoMirrored.Outlined.Article,
            text = stringResource(Res.string.terms_of_service),
            onClick = onTOSClick
        )
        
        SettingRow(
            imageVector = Icons.Outlined.Language,
            text = stringResource(Res.string.language),
            onClick = onLanguageClick
        )
        
        SettingRow(
            imageVector = Icons.Outlined.DarkMode,
            text = stringResource(Res.string.dark_mode),
            trailing = @Composable {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it; onThemeSwitchClick(it) },
                    modifier = Modifier.height(24.dp)
                )
            }
        )
    }
}