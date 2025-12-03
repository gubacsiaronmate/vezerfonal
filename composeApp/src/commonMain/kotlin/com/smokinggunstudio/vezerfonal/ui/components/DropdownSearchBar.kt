package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.DTO
import com.smokinggunstudio.vezerfonal.data.OrgData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.organization_name
import vezerfonal.composeapp.generated.resources.remember_me

@Composable
internal inline fun <reified T : DTO> DropdownSearchBar(
    allItems: List<T>,
    labelText: String,
    onItemSelected: CallbackEvent<T>
) {
    var expanded by remember { mutableStateOf(false) }
    var filtered by remember(allItems) { mutableStateOf(allItems) }
    var selectedItem by remember { mutableStateOf("") }
    
    Box(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {
                selectedItem = it
                filtered = allItems
                    .filter { item -> item.name.contains(it, ignoreCase = true) }
                    .sortedWith(
                        compareBy<T> { item ->
                            item.name.removePrefix(it).length
                        }.thenBy { item -> item.name }
                    )
            },
            label = {
                Text(
                    text = labelText,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .onFocusChanged { expanded = it.isFocused },
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filtered.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = { selectedItem = item.name; onItemSelected(item) }
                )
            }
        }
        
    }
}