package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Outbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.components.SettingsNameCard
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import com.smokinggunstudio.vezerfonal.ui.helpers.LocalWindowSizeInfo
import com.smokinggunstudio.vezerfonal.ui.helpers.WindowWidthClass
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun SettingsScreen(
    user: UserData,
    onAccountSettingsClick: Function,
    onAdminToolsClick: Function,
    onArchiveClick: Function,
    onNotificationsClick: Function,
    onTOSClick: Function,
    onLanguageClick: Function,
    onSentMessagesClick: Function,
    isInDarkTheme: Boolean,
    onThemeSwitchClick: CallbackFunction<Boolean>,
) {
    var checked by remember { mutableStateOf(isInDarkTheme) }
    val isExpanded = LocalWindowSizeInfo.current.widthClass == WindowWidthClass.Expanded

    val settingsContent: @Composable ColumnScope.() -> Unit = {
        SettingsNameCard(user.name, onAccountSettingsClick)

        HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))

        // Account section
        if (user.isSuperAdmin || user.isAnyAdmin) {
            Spacer(Modifier.height(Spacing.sm))
            Text(
                text = stringResource(Res.string.admin_tools),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
            )
            if (user.isSuperAdmin)
                SettingRow(
                    imageVector = Icons.Outlined.AdminPanelSettings,
                    text = stringResource(Res.string.admin_tools),
                    onClick = onAdminToolsClick,
                )
            if (user.isAnyAdmin)
                SettingRow(
                    imageVector = Icons.Outlined.Outbox,
                    text = stringResource(Res.string.sent_messages),
                    onClick = onSentMessagesClick,
                )
            HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))
        }

        // Content section
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(Res.string.settings_section_content),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        SettingRow(
            imageVector = Icons.Outlined.Archive,
            text = stringResource(Res.string.archive),
            onClick = onArchiveClick,
        )
        SettingRow(
            imageVector = Icons.Outlined.Notifications,
            text = stringResource(Res.string.notifications),
            onClick = onNotificationsClick,
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))

        // Appearance section
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(Res.string.settings_section_appearance),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        SettingRow(
            imageVector = Icons.Outlined.Language,
            text = stringResource(Res.string.language),
            onClick = onLanguageClick,
        )
        SettingRow(
            imageVector = Icons.Outlined.DarkMode,
            text = stringResource(Res.string.dark_mode),
            trailing = @Composable {
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it; onThemeSwitchClick(it) },
                    modifier = Modifier.height(24.dp),
                )
            }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.lg))

        // Legal section
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = stringResource(Res.string.settings_section_legal),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.xs),
        )
        SettingRow(
            imageVector = Icons.AutoMirrored.Outlined.Article,
            text = stringResource(Res.string.terms_of_service),
            onClick = onTOSClick,
        )

        Spacer(Modifier.height(Spacing.xl))
    }

    if (isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.xl),
            contentAlignment = Alignment.TopCenter,
        ) {
            Card(
                modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                ) { settingsContent() }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) { settingsContent() }
    }
}
