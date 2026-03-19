package com.smokinggunstudio.vezerfonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import com.smokinggunstudio.vezerfonal.LocalPreferenceStorage
import com.smokinggunstudio.vezerfonal.ui.screens.ArchiveOptionsScreen

object ArchiveOptions : Screen {
    @Composable
    override fun Content() {
        val prefStorage = LocalPreferenceStorage.current

        var isEnabled by remember { mutableStateOf(prefStorage.getArchiveEnabled()) }
        var minStatus by remember { mutableStateOf(prefStorage.getArchiveMinStatus()) }
        var delayHours by remember { mutableIntStateOf(prefStorage.getArchiveDelayHours()) }

        ArchiveOptionsScreen(
            isEnabled = isEnabled,
            minStatus = minStatus,
            delayHours = delayHours,
            onEnabledChange = { value ->
                isEnabled = value
                prefStorage.saveArchiveEnabled(value)
            },
            onMinStatusChange = { status ->
                minStatus = status
                prefStorage.saveArchiveMinStatus(status)
            },
            onDelayChange = { hours ->
                delayHours = hours
                prefStorage.saveArchiveDelayHours(hours)
            },
        )
    }
}
