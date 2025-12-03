package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.cancel
import vezerfonal.composeapp.generated.resources.create
import vezerfonal.composeapp.generated.resources.create_group
import vezerfonal.composeapp.generated.resources.group_name
import vezerfonal.composeapp.generated.resources.name

@Composable
fun CreateDialog(
    titleText: String,
    onCancelClick: ClickEvent,
    onCreateClick: ClickEvent,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        verticalArrangement = verticalArrangement,
        modifier = modifier
    ) {
        Text(
            text = titleText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        
        content()
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onCancelClick) { Text(stringResource(Res.string.cancel)) }
            Button(onCreateClick) { Text(stringResource(Res.string.create)) }
        }
    }
}