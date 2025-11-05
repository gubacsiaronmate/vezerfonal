package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.components.PfpSetter
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.accept_terms
import vezerfonal.composeapp.generated.resources.create_account
import vezerfonal.composeapp.generated.resources.display_name
import vezerfonal.composeapp.generated.resources.identifier

@Composable fun ProfileCreationScreen(
    registerState: RegisterState,
    onClick: ClickEvent
) {
    var areTermsAccepted by remember { mutableStateOf(false) }
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 8.dp),
    ) {
        RegisterText()
        
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = registerState.identifier,
                onValueChange = { registerState.updateIdentifier(it) },
                label = { Text(stringResource(Res.string.identifier)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = registerState.name,
                onValueChange = { registerState.updateName(it) },
                label = { Text(stringResource(Res.string.display_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            PfpSetter(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = areTermsAccepted,
                    onCheckedChange = { areTermsAccepted = it }
                )
                Text(stringResource(Res.string.accept_terms))
            }
            Button(
                onClick = onClick,
                enabled = areTermsAccepted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) { Text(stringResource(Res.string.create_account)) }
        }
    }
}