package com.smokinggunstudio.vezerfonal.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.ui.helpers.CallbackFunction
import com.smokinggunstudio.vezerfonal.ui.helpers.Function
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.description
import vezerfonal.composeapp.generated.resources.edit_group
import vezerfonal.composeapp.generated.resources.group_name
import vezerfonal.composeapp.generated.resources.name

@Composable
fun GroupEditDialog(
    group: GroupData,
    users: List<UserData>,
    modifier: Modifier = Modifier,
    onCancelClick: Function,
    onApplyClick: CallbackFunction<GroupData>,
) {
    var groupName by remember { mutableStateOf(group.name) }
    var description by remember { mutableStateOf(group.description) }
    var adminIdentifier by remember { mutableStateOf(group.adminIdentifier) }

    CreateDialog(
        titleText = stringResource(Res.string.edit_group),
        onCancelClick = onCancelClick,
        onCreateClick = {
            onApplyClick(group.copy(name = groupName, description = description, adminIdentifier = adminIdentifier))
            onCancelClick()
        },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text(stringResource(Res.string.group_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(Res.string.description)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
        )

        DropdownSearchBar(
            allItems = users,
            labelText = stringResource(Res.string.name),
        ) { adminIdentifier = it.externalId }
    }
}
