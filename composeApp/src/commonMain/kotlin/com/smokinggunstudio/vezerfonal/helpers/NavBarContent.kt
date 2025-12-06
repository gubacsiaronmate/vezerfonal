package com.smokinggunstudio.vezerfonal.helpers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.smokinggunstudio.vezerfonal.ui.helpers.capitalize
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

sealed class NavBarContent {
    data object Home: NavBarContent()
    data object Archive: NavBarContent()
    data object Send: NavBarContent()
    data object Group: NavBarContent()
    data object Settings: NavBarContent()
    
    fun icon(filled: Boolean): ImageVector =
        if (filled) when (this) {
            Home -> Icons.Filled.Home
            Archive -> Icons.Filled.Archive
            Send -> Icons.Filled.AddComment
            Group -> Icons.Filled.Groups2
            Settings -> Icons.Filled.Settings
        } else when (this) {
            Home -> Icons.Outlined.Home
            Archive -> Icons.Outlined.Archive
            Send -> Icons.Outlined.AddComment
            Group -> Icons.Outlined.Groups2
            Settings -> Icons.Outlined.Settings
        }
}