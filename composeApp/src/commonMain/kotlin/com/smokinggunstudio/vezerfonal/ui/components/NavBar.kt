package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.resolveLabel
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction

@Composable
fun NavBar(
    tabs: List<NavBarContent>,
    currentIndex: Int,
    onTabSelected: CallbackFunction<Int>,
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 4.dp,
    ) {
        tabs.forEachIndexed { i, tab ->
            val selected = i == currentIndex
            val label = tab.resolveLabel()
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(i) },
                icon = {
                    Icon(
                        imageVector = tab.icon(selected),
                        contentDescription = label,
                    )
                },
                label = { Text(label) },
                alwaysShowLabel = false,
            )
        }
    }
}
