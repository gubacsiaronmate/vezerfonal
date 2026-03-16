package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.NavBarContent
import com.smokinggunstudio.vezerfonal.helpers.resolveLabel
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.theme.Spacing
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.vezerfonal

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
                Text(
                    text = stringResource(Res.string.vezerfonal),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = Spacing.lg, vertical = Spacing.md),
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.sm))
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
