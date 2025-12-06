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
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.getExtId
import com.smokinggunstudio.vezerfonal.network.api.createGroup
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackEvent
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.cancel
import vezerfonal.composeapp.generated.resources.create
import vezerfonal.composeapp.generated.resources.create_group
import vezerfonal.composeapp.generated.resources.group_name
import vezerfonal.composeapp.generated.resources.name
import kotlin.collections.plus

@Composable fun CreateGroupDialog(
    client: HttpClient,
    accessToken: String,
    users: List<UserData>,
    onCancelClick: ClickEvent,
    onCreatedGroup: CallbackEvent<GroupData>,
) {
    val scope = rememberCoroutineScope()
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var adminIdentifier by remember { mutableStateOf("") }
    
    CreateDialog(
        titleText = stringResource(Res.string.create_group),
        onCancelClick = onCancelClick,
        onCreateClick = {
            val group = GroupData(
                name = groupName,
                externalId = getExtId(),
                description = description,
                members = emptyList(),
                adminIdentifier = adminIdentifier
            )
            
            scope.launch {
                if (createGroup(
                    groupData = group,
                    accessToken = accessToken,
                    client = client
                )) {
                    onCreatedGroup(group)
                    onCancelClick()
                }
            }
        }
    ) {
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text(stringResource(Res.string.group_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
        
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(Res.string.group_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
        
        DropdownSearchBar(
            allItems = users,
            labelText = stringResource(Res.string.name)
        ) { adminIdentifier = it.identifier }
    }
}