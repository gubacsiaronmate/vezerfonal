package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.DTO
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackClickEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ComposableContent

@Composable
inline fun <reified T : DTO> SelectionListItem(
    item: T,
    isChecked: Boolean,
    noinline prefixContent: ComposableContent? = null,
    onCheckedChange: CallbackClickEvent<Boolean>
) {
    var checked by remember(isChecked) { mutableStateOf(isChecked) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (prefixContent != null) prefixContent()
        Text(
            text = item.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it; onCheckedChange(it) }
        )
    }
}