package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.SettingRow
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.admin_tools
import vezerfonal.composeapp.generated.resources.registration_code_management
import vezerfonal.composeapp.generated.resources.tag_management
import vezerfonal.composeapp.generated.resources.tags
import vezerfonal.composeapp.generated.resources.user_management

@Preview(showBackground = true)
@Composable
fun AdminToolsScreen(
    onUserManagementClick: ClickEvent,
    onTagManagementClick: ClickEvent,
    onRegistrationCodeManagementClick: ClickEvent
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface)
        .padding(8.dp)) {
        Text(text = stringResource(Res.string.admin_tools),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(8.dp))
        SettingRow(
            imageVector = Icons.Outlined.Person,
            text = stringResource(Res.string.user_management),
            onClick = onUserManagementClick
        )
        SettingRow(
            imageVector = Icons.Outlined.Sell,
            text = stringResource(Res.string.tag_management),
            onClick = onTagManagementClick
        )
        SettingRow(
            imageVector = Icons.Outlined.Key,
            text = stringResource(Res.string.registration_code_management),
            onClick = onRegistrationCodeManagementClick
        )
    }
}