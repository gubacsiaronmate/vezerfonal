package com.smokinggunstudio.vezerfonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smokinggunstudio.vezerfonal.helpers.FileData
import com.smokinggunstudio.vezerfonal.helpers.security.TokenStorage
import com.smokinggunstudio.vezerfonal.network.api.registerBasic
import com.smokinggunstudio.vezerfonal.ui.components.AnimatedButton
import com.smokinggunstudio.vezerfonal.ui.components.PfpSetter
import com.smokinggunstudio.vezerfonal.ui.components.RegisterText
import com.smokinggunstudio.vezerfonal.ui.helpers.ClickEvent
import com.smokinggunstudio.vezerfonal.ui.state.RegisterState
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import vezerfonal.composeapp.generated.resources.*

@Composable fun ProfileCreationScreen(
    registerState: RegisterState,
    tokenStorage: TokenStorage,
    client: HttpClient,
    onClick: ClickEvent
) {
    var areTermsAccepted by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var data: FileData? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp),
    ) {
        RegisterText()
        
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = registerState.identifier,
                onValueChange = { registerState.updateIdentifier(it) },
                label = { Text(stringResource(Res.string.identifier),
                    color = MaterialTheme.colorScheme.onSurface) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            
            OutlinedTextField(
                value = registerState.name,
                onValueChange = { registerState.updateName(it) },
                label = { Text(stringResource(Res.string.display_name),
                    color = MaterialTheme.colorScheme.onSurface) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp)
            )
            
            PfpSetter(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onFilePickCallBack = { data = it }
            )
            
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceEvenly) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = areTermsAccepted,
                        onCheckedChange = { areTermsAccepted = it; }
                    )
                    Text(
                        text = stringResource(Res.string.accept_terms),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it; }
                    )
                    Text(
                        text = stringResource(Res.string.remember_me),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            AnimatedButton(
                onClick = {
                    scope.launch { 
                        val tokens = if (data == null) throw NullPointerException("Profile picture cannot be null.")
                        else registerBasic(
                            userData = registerState.toUserData(),
                            rememberMe = rememberMe,
                            pfp = data!!,
                            client = client
                        )
                        tokenStorage.saveTokens(tokens)
                        onClick()
                    }
                },
                enabled = (
                    registerState.identifier.isNotBlank()
                    && registerState.name.isNotBlank()
                    && areTermsAccepted
                    && data != null
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) { Text(stringResource(Res.string.create_account)) }
        }
    }
}