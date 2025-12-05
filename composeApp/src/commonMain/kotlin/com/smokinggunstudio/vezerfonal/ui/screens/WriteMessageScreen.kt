package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.network.api.getAllTags
import com.smokinggunstudio.vezerfonal.network.api.getUsersByIdentifierList
import com.smokinggunstudio.vezerfonal.network.api.sendMessage
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.state.GroupSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.TagSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.UserSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.WriteMessageState
import io.ktor.client.HttpClient
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Preview(showBackground = true)
@Composable
fun WriteMessageScreen(
    user: UserData,
    client: HttpClient,
    accessToken: String,
    guiao: List<GroupData>
) {
    val scope = rememberCoroutineScope()
    val state = remember { WriteMessageState() }
    var isGroupTabOpened by remember { mutableStateOf(false) }
    val groupSelectionState = remember { GroupSelectionState() }
    val userSelectionState = remember { UserSelectionState() }
    val tagSelectionState = remember { TagSelectionState() }
    var isIndividualTabOpened by remember { mutableStateOf(false) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    var userList by remember { mutableStateOf<List<UserData>>(emptyList()) }
    var tagList by remember { mutableStateOf<List<TagData>>(emptyList()) }
    
    val job = scope.launch {
        userList = getUsersByIdentifierList(
            guiao
                .map { it.members }
                .flatten(),
            accessToken,
            client
        )
        tagList = getAllTags(accessToken, client)
    }
    
    groupSelectionState.loadAllItems(guiao)
    if (job.isCompleted) {
        userSelectionState.loadAllItems(userList)
        tagSelectionState.loadAllItems(tagList)
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = stringResource(Res.string.to),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RecipientSelectButton(
                text = stringResource(Res.string.groups),
                selectedAmount = state.groups.size,
                onClick = { isGroupTabOpened = true }
            )
            RecipientSelectButton(
                text = stringResource(Res.string.individuals),
                selectedAmount = state.userIdentifiers.size,
                onClick = { isIndividualTabOpened = true }
            )
            IconToggleButton(
                checked = state.isUrgent,
                onCheckedChange = state::updateUrgency
            ) {
                Image(
                    imageVector = if (!state.isUrgent)
                        Icons.Outlined.ErrorOutline
                    else Icons.Filled.Error,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        }
        
        Box {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.title,
                    onValueChange = state::updateTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    label = {
                        Text(
                            text = stringResource(Res.string.subject),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.content,
                    onValueChange = state::updateContent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6F)
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    label = {
                        Text(
                            text = stringResource(Res.string.message),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ReactionBar { emoji ->
                        if (state.availableReactions.contains(emoji))
                            state.removeReaction(emoji)
                        else state.addReaction(emoji)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(Res.string.tags),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    HorizontallyScrollableTagSelect(tagSelectionState) { isTagSelectTabOpened = true }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        onClick = {
                            scope.launch {
                                val message = state.toMessageData(user)
                                sendMessage(
                                    client = client,
                                    message = message,
                                    accessToken = accessToken,
                                )
                                state.clear()
                            }
                        },
                    ) {
                        Image(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = stringResource(Res.string.send),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    }
                }
            }
            
            if (isGroupTabOpened) GroupSelect(
                state = groupSelectionState,
                onCancelClick = { isGroupTabOpened = false },
                onApplyClick = { groups -> state.updateGroups(groups.map { it.externalId }) }
            )
            if (isTagSelectTabOpened) TagSelect(
                state = tagSelectionState,
                onCancelClick = { isTagSelectTabOpened = false },
                onApplyClick = { tags -> state.updateTags(tags.map { it.name }) }
            )
            if (isIndividualTabOpened) IndividualSelect(
                state = userSelectionState,
                onCancelClick = { isIndividualTabOpened = false },
                onApplyClick = { users -> state.updateUserIdentifiers(users.map { it.identifier }) }
            )
        }
    }
}