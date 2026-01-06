package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.controller.TagSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.browse_tags
import vezerfonal.composeapp.generated.resources.tags

@Composable
fun HorizontallyScrollableTagSelect(
    snapshot: TagSelectionStateModel,
    modifier: Modifier = Modifier,
    tabOpenedCallback: ClickEvent,
) {
    val state = remember { TagSelectionStateController(snapshot) }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        state.visibleItems.forEach { tag ->
            val checked by remember { mutableStateOf(tag in state.selectedItems) }
            IconToggleButton(
                checked = checked,
                onCheckedChange = {
                    if (checked) state.removeItem(tag)
                    else state.addItem(tag)
                },
                colors = IconButtonDefaults.iconToggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .width(90.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.tags),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Button(
            onClick = tabOpenedCallback,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(Res.string.browse_tags),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}