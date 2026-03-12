package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.resolveLabel
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction

@Composable
fun AppNavigationRail(
    tabs: List<NavBarContent>,
    currentIndex: Int,
    onTabSelected: CallbackFunction<Int>,
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxHeight(),
    ) {
        Spacer(Modifier.weight(1f))
        tabs.forEachIndexed { i, tab ->
            val selected = i == currentIndex
            val label = tab.resolveLabel()
            NavigationRailItem(
                selected = selected,
                onClick = { onTabSelected(i) },
                icon = {
                    Icon(
                        imageVector = tab.icon(selected),
                        contentDescription = label,
                    )
                },
                label = { Text(label) },
            )
        }
        Spacer(Modifier.weight(1f))
    }
}
