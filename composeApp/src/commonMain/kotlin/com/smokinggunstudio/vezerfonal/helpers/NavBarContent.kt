package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups2
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Groups2
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.capitalize
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.archive
import vezerfonal.composeapp.generated.resources.groups
import vezerfonal.composeapp.generated.resources.groups_label
import vezerfonal.composeapp.generated.resources.home
import vezerfonal.composeapp.generated.resources.send_message
import vezerfonal.composeapp.generated.resources.settings

sealed class NavBarContent {
    data object Home: NavBarContent()
    data object Archive: NavBarContent()
    data object Send: NavBarContent()
    data object Group: NavBarContent()
    data object Settings: NavBarContent()
    
    @Composable fun label(): String = when (this) {
        Home -> stringResource(Res.string.home).capitalize()
        Archive -> stringResource(Res.string.archive).capitalize()
        Send -> stringResource(Res.string.send_message).capitalize()
        Group -> stringResource(Res.string.groups).capitalize()
        Settings -> stringResource(Res.string.settings).capitalize()
    }
    
    fun icon(filled: Boolean): ImageVector =
        if (filled) when (this) {
            Home -> Icons.Filled.Home
            Archive -> Icons.Filled.Archive
            Send -> Icons.Filled.Add
            Group -> Icons.Filled.Groups2
            Settings -> Icons.Filled.Settings
        } else when (this) {
            Home -> Icons.Outlined.Home
            Archive -> Icons.Outlined.Archive
            Send -> Icons.Outlined.Add
            Group -> Icons.Outlined.Groups2
            Settings -> Icons.Outlined.Settings
        }
}