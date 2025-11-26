package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.GroupSelect
import com.smokinggunstudio.vezerfonal.ui.components.HorizontallyScrollableTagSelect
import com.smokinggunstudio.vezerfonal.ui.components.ReactionBar
import com.smokinggunstudio.vezerfonal.ui.components.RecipientSelectButton
import com.smokinggunstudio.vezerfonal.ui.state.GroupSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.TagSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.UserSelectionState
import com.smokinggunstudio.vezerfonal.ui.state.WriteMessageState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Preview(showBackground = true)
@Composable
fun WriteMessageScreen() {
    val state = remember { WriteMessageState() }
    var isGroupTabOpened by remember { mutableStateOf(false) }
    val groupSelectionState = remember { GroupSelectionState() }
    val userSelectionState = remember { UserSelectionState() }
    val tagSelectionState = remember { TagSelectionState() }
    var isIndividualTabOpened by remember { mutableStateOf(true) }
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = stringResource(Res.string.to),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RecipientSelectButton(
                text = stringResource(Res.string.groups),
                onClick = { isGroupTabOpened = it }
            )
            RecipientSelectButton(
                text = stringResource(Res.string.individuals),
                onClick = { isIndividualTabOpened = it }
            )
            IconToggleButton(
                checked = state.isUrgent,
                onCheckedChange = state::updateUrgency
            ) {
                Image(
                    imageVector = if (!state.isUrgent)
                        Icons.Outlined.ErrorOutline
                    else Icons.Outlined.Error,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        }
        
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = state.subject,
                    onValueChange = state::updateSubject,
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
                    value = state.message,
                    onValueChange = state::updateMessage,
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
                        if (state.reactions.contains(emoji)) {
                            state.removeReaction(emoji)
                        } else {
                            state.addReaction(emoji)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(Res.string.tags),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    HorizontallyScrollableTagSelect(tagSelectionState) { _ -> }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
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
            
            if (isGroupTabOpened) GroupSelect(groupSelectionState)
        }
    }
}