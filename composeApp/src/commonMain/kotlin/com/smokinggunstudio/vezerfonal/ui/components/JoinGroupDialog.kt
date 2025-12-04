package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.network.api.joinGroup
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun JoinGroupDialog(
    accessToken: String,
    client: HttpClient,
    onCancelClick: ClickEvent,
    onGroupJoined: CallbackEvent<GroupData>
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
                    val group = joinGroup(
                        extId = groupExtId,
                        accessToken = accessToken,
                        client = client
                    )
                    onGroupJoined(group)
                    onCancelClick()
                }
            }) { Text(stringResource(Res.string.join)) }
        }
    }
}