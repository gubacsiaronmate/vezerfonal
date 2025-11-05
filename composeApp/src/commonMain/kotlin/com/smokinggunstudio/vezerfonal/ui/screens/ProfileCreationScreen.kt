package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.network.api.registerBasic
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.components.PfpSetter
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.Res
import vezerfonal.composeapp.generated.resources.accept_terms
import vezerfonal.composeapp.generated.resources.create_account
import vezerfonal.composeapp.generated.resources.display_name
import vezerfonal.composeapp.generated.resources.identifier

@Composable fun ProfileCreationScreen(
    registerState: RegisterState,
    client: HttpClient,
    onClick: ClickEvent
) {
    var areTermsAccepted by remember { mutableStateOf(false) }
    var data: FileData? = null
    val scope = rememberCoroutineScope()
    
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
                modifier = Modifier.fillMaxWidth(),
                onFilePickCallBack = { data = it }
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = areTermsAccepted,
                    onCheckedChange = { areTermsAccepted = it }
                )
                Text(
                    text = stringResource(Res.string.accept_terms),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Button(
                onClick = {
                    scope.launch { 
                        if (data == null) throw NullPointerException("Profile picture cannot be null.")
                        registerBasic(
                            userData = registerState.toUserData(),
                            pfp = data!!,
                            client = client
                        )
                    }
                    onClick()
                },
                enabled = areTermsAccepted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) { Text(stringResource(Res.string.create_account)) }
        }
    }
}