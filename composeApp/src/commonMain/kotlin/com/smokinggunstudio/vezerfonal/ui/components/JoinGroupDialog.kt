package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.network.api.joinGroup
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.cancel
import vezerfonal.composeapp.generated.resources.group_name
import vezerfonal.composeapp.generated.resources.join
import vezerfonal.composeapp.generated.resources.join_group

@Composable
fun JoinGroupDialog(
    accessToken: String,
    client: HttpClient,
    onCancelClick: ClickEvent
) {
    val scope = rememberCoroutineScope()
    var groupExtId by remember { mutableStateOf("") }
    
    Dialog {
        Text(
            text = stringResource(Res.string.join_group),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        
        OutlinedTextField(
            value = groupExtId,
            onValueChange = { groupExtId = it },
            label = { Text(stringResource(Res.string.group_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onCancelClick) { Text(stringResource(Res.string.cancel)) }
            Button({
                scope.launch {
                    joinGroup(
                        extId = groupExtId,
                        accessToken = accessToken,
                        client = client
                    )
                }
                onCancelClick()
            }) { Text(stringResource(Res.string.join)) }
        }
    }
}