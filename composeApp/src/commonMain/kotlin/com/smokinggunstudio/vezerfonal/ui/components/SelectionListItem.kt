package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.smokinggunstudio.vezerfonal.data.NamedDTO
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable
inline fun <reified T : NamedDTO> SelectionListItem(
    item: T,
    isChecked: Boolean,
    noinline prefixContent: ComposableContent? = null,
    onCheckedChange: CallbackFunction<Boolean>
) {
    var checked by remember(isChecked) { mutableStateOf(isChecked) }
    ListItem(
        headlineContent = {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        leadingContent = prefixContent,
        trailingContent = {
            Checkbox(checked = checked, onCheckedChange = null)
        },
        modifier = Modifier.clickable {
            checked = !checked
            onCheckedChange(checked)
        },
    )
}
