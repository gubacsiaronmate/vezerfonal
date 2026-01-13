package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.*
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
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun JoinGroupDialog(
    accessToken: String,
    client: HttpClient,
    onCancelClick: Function,
    onGroupJoined: CallbackFunction<GroupData>
) {
    val scope = rememberCoroutineScope()
    var groupExtId by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<Throwable?>(null)}
    
    Box(Modifier.fillMaxSize()) {
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
                    try {
                        scope.launch {
                            val group = joinGroup(
                                extId = groupExtId,
                                accessToken = accessToken,
                                client = client
                            )
                            onGroupJoined(group)
                            onCancelClick()
                        }
                    } catch (e: Exception) {
                        error = e
                    }
                }) { Text(stringResource(Res.string.join)) }
            }
        }
        if (error != null) ErrorDialog(error!!.message!!, true)
    }
}