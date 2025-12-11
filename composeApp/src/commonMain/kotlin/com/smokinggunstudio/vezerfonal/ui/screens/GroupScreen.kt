package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.Identifier
import com.smokinggunstudio.vezerfonal.network.api.getAllUsers
import com.smokinggunstudio.vezerfonal.ui.components.CreateGroupDialog
import com.smokinggunstudio.vezerfonal.ui.components.GroupCard
import com.smokinggunstudio.vezerfonal.ui.components.JoinGroupDialog
import com.smokinggunstudio.vezerfonal.ui.components.SwipeableGroupCard
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.groups
import vezerfonal.composeapp.generated.resources.join_group

@Composable fun GroupScreen(
    client: HttpClient,
    accessToken: String,
    myIdentifier: Identifier,
    groupData: List<GroupData>,
    isSuperAdminLogIn: Boolean = false
) {
    val scope = rememberCoroutineScope()
    var groups by remember(groupData) { mutableStateOf(groupData) }
    var isCreatePopUpOn by remember { mutableStateOf(false) }
    var isJoinPopUpOn by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<UserData>?>(null) }
    var loaded by remember { mutableStateOf(false) }
    
    if (isSuperAdminLogIn) LaunchedEffect(Unit) {
        val d = getAllUsers(accessToken, client)
        users = d
        loaded = true
    } else loaded = true
    
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface)
        .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.groups),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
                
                Button(
                    onClick = { isJoinPopUpOn = true },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Add, null)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(Res.string.join_group),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            groups.forEach { group ->
                if (isSuperAdminLogIn)
                    SwipeableGroupCard(
                        onEdit = { },
                        onDelete = {
                            groups = groups.filter { it != group }
                            scope.launch {
                                // TODO: Add logic to delete group
                            }
                        },
                        group = group,
                        myIdentifier = myIdentifier
                    )
                else GroupCard(
                    name = group.name,
                    extId = group.externalId,
                    description = group.description,
                    amITheAdmin = group.adminIdentifier == myIdentifier
                )
            }
        }
        
        if (isSuperAdminLogIn) IconButton(
            onClick = { isCreatePopUpOn = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center){
            if (isSuperAdminLogIn && isCreatePopUpOn && loaded)
                CreateGroupDialog(
                    client = client,
                    accessToken = accessToken,
                    users = users!!,
                    onCancelClick = { isCreatePopUpOn = false }
                ) { groups += it }
            
            if (isJoinPopUpOn) JoinGroupDialog(
                accessToken = accessToken,
                client = client,
                { isJoinPopUpOn = false }
            ) { groups += it }
        }
    }
}