package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.NamedDTO
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent

@Composable
internal inline fun <reified T : NamedDTO> DropdownSearchBar(
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
                .padding(horizontal = 4.dp)
                .onFocusChanged { expanded = it.isFocused },
        )
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filtered.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        selectedItem = item.name
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}