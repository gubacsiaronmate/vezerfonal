package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.resolveLabel
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing

@Composable
fun AppNavigationDrawer(
    tabs: List<NavBarContent>,
    currentIndex: Int,
    onTabSelected: CallbackFunction<Int>,
    content: @Composable () -> Unit,
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                Spacer(Modifier.height(Spacing.xl))
                tabs.forEachIndexed { i, tab ->
                    val selected = i == currentIndex
                    val label = tab.resolveLabel()
                    NavigationDrawerItem(
                        label = { Text(label) },
                        icon = {
                            Icon(
                                imageVector = tab.icon(selected),
                                contentDescription = null,
                            )
                        },
                        selected = selected,
                        onClick = { onTabSelected(i) },
                        modifier = Modifier.padding(horizontal = Spacing.sm),
                    )
                }
            }
        },
        content = content,
    )
}
