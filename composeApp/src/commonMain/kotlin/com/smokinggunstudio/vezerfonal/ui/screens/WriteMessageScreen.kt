package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.components.RecipientSelectButton
import com.smokinggunstudio.vezerfonal.ui.state.WriteMessageState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vezerfonal.composeapp.generated.resources.*

@Preview(showBackground = true)
@Composable
fun WriteMessageScreen() {
    val state = WriteMessageState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(Res.string.to),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )
            RecipientSelectButton(
                text = stringResource(Res.string.groups),
                onClick = {}
            )
            RecipientSelectButton(
                text = stringResource(Res.string.individuals),
                onClick = {}
            )
            IconToggleButton(
                modifier = Modifier
                    .size(48.dp),
                checked = false,
                onCheckedChange = {}) {
                Image(
                    imageVector = Icons.Default.AssignmentLate,
                    contentDescription = null,
                )
            }
        }
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
        }
    }
}