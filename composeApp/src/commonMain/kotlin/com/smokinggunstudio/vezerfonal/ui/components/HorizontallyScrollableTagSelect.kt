package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.browse_tags

@Composable
fun HorizontallyScrollableTagSelect(
    tagList: List<String>,
    tabOpenedCallback: Function,
    onTagSelectionChange: CallbackFunction<Pair<Boolean, String>>
) {
    LazyRow {
        items(tagList) { tag ->
            var checked by remember { mutableStateOf(false) }
            IconToggleButton(
                colors = IconButtonDefaults.iconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    checkedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                checked = checked,
                onCheckedChange = {
                    onTagSelectionChange(Pair(checked, tag))
                    checked = !checked
                },
                modifier = Modifier.padding(8.dp).widthIn(120.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    val icon =
                        if (checked)
                            Icons.Filled.Sell
                    else Icons.Outlined.Sell
                    
                    Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    Spacer(Modifier.size(6.dp))
                    
                    Text(
                        text = tag,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            Button(
                onClick = tabOpenedCallback,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceContainer),
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
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
                    
                    Spacer(Modifier.size(6.dp))
                    
                    Text(
                        text = stringResource(Res.string.browse_tags),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}