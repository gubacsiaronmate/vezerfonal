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
    /** Non-composable fallback label — used for accessibility descriptions. */
    abstract val label: String

    data object Home: NavBarContent()     { override val label = "Home" }
    data object Archive: NavBarContent()  { override val label = "Archive" }
    data object Send: NavBarContent()     { override val label = "Write message" }
    data object Group: NavBarContent()    { override val label = "Groups" }
    data object Settings: NavBarContent() { override val label = "Settings" }

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

/** Returns the localized label for this navigation tab. */
@Composable
fun NavBarContent.resolveLabel(): String = stringResource(
    when (this) {
        NavBarContent.Home     -> Res.string.home
        NavBarContent.Archive  -> Res.string.archive
        NavBarContent.Send     -> Res.string.write_message
        NavBarContent.Group    -> Res.string.groups
        NavBarContent.Settings -> Res.string.settings
    }
)