package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.data.GroupData
import com.smokinggunstudio.vezerfonal.data.TagData
import com.smokinggunstudio.vezerfonal.data.UserData
import com.smokinggunstudio.vezerfonal.helpers.UnauthorizedException
import com.smokinggunstudio.vezerfonal.network.api.sendMessage
import com.smokinggunstudio.vezerfonal.ui.components.*
import com.smokinggunstudio.vezerfonal.ui.helpers.contains
import com.smokinggunstudio.vezerfonal.ui.state.controller.GroupSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.TagSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.UserSelectionStateController
import com.smokinggunstudio.vezerfonal.ui.state.controller.WriteMessageStateController
import com.smokinggunstudio.vezerfonal.ui.state.model.GroupSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.TagSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.UserSelectionStateModel
import com.smokinggunstudio.vezerfonal.ui.state.model.WriteMessageStateModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable
fun WriteMessageScreen(
    user: UserData,
    client: HttpClient,
    accessToken: String,
    guiao: List<GroupData>,
    userList: List<UserData>,
    tagList: List<TagData>
) {
    val scope = rememberCoroutineScope()
    val state = remember { WriteMessageStateController(WriteMessageStateModel()) }
    var isGroupTabOpened by remember { mutableStateOf(false) }
    var isIndividualTabOpened by remember { mutableStateOf(false) }
    var isTagSelectTabOpened by remember { mutableStateOf(false) }
    val groupSelectionState = remember { GroupSelectionStateController(GroupSelectionStateModel()) }
    val userSelectionState = remember { UserSelectionStateController(UserSelectionStateModel()) }
    val tagSelectionState = remember { TagSelectionStateController(TagSelectionStateModel()) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    
    groupSelectionState.loadAllItems(guiao)
    userSelectionState.loadAllItems(userList)
    tagSelectionState.loadAllItems(tagList)
    
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                            .padding(top = 12.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ReactionBar(state.availableReactions) { i, emoji ->
                            if (state.availableReactions.contains(emoji))
                                state.removeReaction(emoji, i)
                            else state.addReaction(emoji, i)
                        }
                        
                        Spacer(Modifier.height(12.dp))
                        
                        HorizontallyScrollableTagSelect(
                            tagSelectionState.snapshot() as TagSelectionStateModel
                        ) { isTagSelectTabOpened = true }
                        
                        Spacer(Modifier.height(12.dp))
                        
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                            onClick = {
                                try {
                                    scope.launch {
                                        val message = state.toMessageData(user)
                                        sendMessage(
                                            client = client,
                                            message = message,
                                            accessToken = accessToken,
                                        )
                                        state.clear()
                                    }
                                } catch (e: UnauthorizedException) {
                                    error = e
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
                
                if (isGroupTabOpened && !isIndividualTabOpened && !isTagSelectTabOpened)
                    GroupSelect(
                        snapshot = groupSelectionState.snapshot() as GroupSelectionStateModel,
                        onCancelClick = { isGroupTabOpened = false },
                        onApplyClick = { groups -> state.updateGroups(groups.map { it.externalId }) }
                    )
                
                if (isTagSelectTabOpened && !isGroupTabOpened && !isIndividualTabOpened)
                    TagSelect(
                        snapshot = tagSelectionState.snapshot() as TagSelectionStateModel,
                        onCancelClick = { isTagSelectTabOpened = false },
                        onApplyClick = { tags -> state.updateTags(tags.map { it.name }) }
                    )
                
                if (isIndividualTabOpened && !isGroupTabOpened && !isTagSelectTabOpened)
                    IndividualSelect(
                        snapshot = userSelectionState.snapshot() as UserSelectionStateModel,
                        onCancelClick = { isIndividualTabOpened = false },
                        onApplyClick = { users -> state.updateUserIdentifiers(users.map { it.externalId }) }
                    )
            }
        }
        if (error != null) ErrorDialog(error!!)
    }
}